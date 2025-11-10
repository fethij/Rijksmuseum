package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.common.SnackBarState
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.domain.DownloadImageUseCase
import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.navigation.ArtDetailScreen
import com.tewelde.rijksmuseum.core.permissions.Permission
import com.tewelde.rijksmuseum.core.permissions.PermissionState
import com.tewelde.rijksmuseum.core.permissions.PermissionsController
import com.tewelde.rijksmuseum.core.permissions.performPermissionedAction
import com.tewelde.rijksmuseum.feature.detail.model.DetailEvent
import com.tewelde.rijksmuseum.feature.detail.model.DetailUiState
import com.tewelde.rijksmuseum.feature.detail.model.State
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.saving_failed
import com.tewelde.rijksmuseum.resources.saving_success
import com.tewelde.rijksmuseum.resources.settings
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@AssistedInject
class DetailPresenter(
    @Assisted val navigator: Navigator,
    @Assisted val screen: ArtDetailScreen,
    val scope: CoroutineScope,
    val getArtDetail: GetArtDetailUseCase,
    private val fileUtil: FileUtil,
    private val permissionsController: PermissionsController,
    private val downloadImageUseCase: DownloadImageUseCase,
    private val snackBarState: SnackBarState
) : Presenter<DetailUiState> {

    @CircuitInject(ArtDetailScreen::class, UiScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator, screen: ArtDetailScreen): DetailPresenter
    }

    val logger = Logger.withTag(this::class.simpleName!!)

    var isDownloading by mutableStateOf(false)
    var downloadProgress: Int by mutableStateOf(0)
    var state by mutableStateOf(DetailUiState(snackbarHostState = snackBarState.snackbarHostState))

    @Composable
    override fun present(): DetailUiState {
        state = produceState(state) {
            value = when (val art = getArtDetail(screen.artId)) {
                is Either.Left -> DetailUiState(
                    snackbarHostState = snackBarState.snackbarHostState,
                    isDownloading = isDownloading,
                    downloadProgress = downloadProgress,
                    state = State.Error(art.value),
                    eventSink = ::onEvent
                )

                is Either.Right -> DetailUiState(
                    snackbarHostState = snackBarState.snackbarHostState,
                    isDownloading = isDownloading,
                    downloadProgress = downloadProgress,
                    state = State.Success(art.value),
                    eventSink = ::onEvent
                )
            }
        }.value
        return state
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnSave -> {
                scope.launch {
                    runCatching {
                        ensureStoragePermissionAndExecute {
                            saveImage(event.context)
                        }
                    }.onFailure {
                        logger.e { "Error saving image: ${it.message}" }
                        isDownloading = false
                        showPermissionErrorMessage()
                    }
                }
            }

            is DetailEvent.NavigateToSettings -> {
                permissionsController.openAppSettings()
            }

            DetailEvent.OnBackClick -> navigator.pop()
        }
    }

    private fun saveImage(context: PlatformContext) = scope.launch {
        isDownloading = true

        val imageLoader = SingletonImageLoader.get(context)
        val cache = imageLoader.diskCache?.openSnapshot(art.url)
        if (cache == null) {
            logger.d { "image not found in cache, Downloading image" }
            val bytes = downloadImageUseCase(art.url) { downloaded, total ->
                total?.let {
                    downloadProgress = (downloaded * 100 / total).toInt().also {
                        Logger.d { "#### download progress: $it" }

                    }
                }
            }.toByteArray()
            saveFile(
                bytes = bytes,
                baseName = art.title ?: "image",
                extension = "jpg", // TODO get extension from file metadata
                onFailure = {
                    isDownloading = false
                    scope.launch {
                        showSavingFailedMessage()
                    }
                },
                onSuccess = {
                    isDownloading = false
                    if (!web) { // TODO: check if image is saved or canceled before enabling this for all platforms
                        scope.launch {
                            showSavingSuccessMessage()
                        }
                    }
                }
            )
        } else {
            logger.d { "image found in cache" }
            cache.use { snapshot ->
                val data = snapshot.data
                val content = fileUtil.filesystem()?.read(data) {
                    readByteArray()
                }

                if (content == null) return@use

                saveFile(
                    bytes = content,
                    baseName = art.title ?: "image",
                    extension = "jpg", // TODO get extension from file metadata
                    onFailure = {
                        isDownloading = false
                        scope.launch {
                            showSavingFailedMessage()
                        }
                    },
                    onSuccess = {
                        isDownloading = false

                        if (!web) { // TODO: check if image is saved or canceled before enabling this for all platforms
                            scope.launch {
                                showSavingSuccessMessage()
                            }
                        }
                    }
                )
            }
        }
    }

    suspend fun ensureStoragePermissionAndExecute(block: suspend () -> Unit) {
        if (!fileUtil.shouldAskStorageRuntimePermission()) {
            block()
            return
        }

        if (!permissionsController.isPermissionGranted(Permission.STORAGE)) {
            permissionsController.performPermissionedAction(Permission.STORAGE) { state ->
                if (state == PermissionState.Granted) {
                    block()
                } else {
                    isDownloading = false
                    showPermissionErrorMessage()
                }
            }
        } else {
            block()
        }
    }

    private val art: ArtObject
        get() = (state.state as State.Success).art

    private fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        onFailure: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = { }
    ) = scope.launch {
        fileUtil.saveFile(
            bytes = bytes,
            baseName = baseName,
            extension = extension,
            onFailure = { onFailure(it) },
            onSuccess = { onSuccess() }
        )
    }

    suspend fun showPermissionErrorMessage() {
        snackBarState.showSnackbar(
            getString(permissionDeniedMessage),
            SnackbarDuration.Long,
            getString(Res.string.settings),
        ) {
            onEvent(DetailEvent.NavigateToSettings)
        }
    }

    suspend fun showSavingSuccessMessage() {
        snackBarState.showSnackbar(
            getString(Res.string.saving_success),
            duration = SnackbarDuration.Short
        )
    }

    suspend fun showSavingFailedMessage() {
        snackBarState.showSnackbar(
            getString(Res.string.saving_failed),
            SnackbarDuration.Short
        )
    }
}