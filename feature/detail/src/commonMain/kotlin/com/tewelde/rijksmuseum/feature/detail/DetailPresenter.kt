package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.domain.DownloadImageUseCase
import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.navigation.ArtDetailScreen
import com.tewelde.rijksmuseum.core.navigation.SnackBarState
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
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.getString

@CircuitInject(ArtDetailScreen::class, UiScope::class)
@Inject
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
    val logger = Logger.withTag(this::class.simpleName!!)
    var state by mutableStateOf(
        DetailUiState(
            snackbarHostState = snackBarState.snackbarHostState,
            eventSink = ::onEvent
        )
    )

    @Composable
    override fun present(): DetailUiState {
        LaunchedEffect(Unit) {
            state = when (val art = getArtDetail(screen.artId)) {
                is Either.Left -> state.copy(
                    state = State.Error(art.value)
                )

                is Either.Right -> state.copy(
                    state = State.Success(art.value),
                )
            }
        }
        return state
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnSave -> {
                scope.launch {
                    ensureStoragePermissionAndExecute {
                        saveImage(event.context)
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
        state = state.copy(isDownloading = true)

        val imageLoader = SingletonImageLoader.get(context)
        val cache = imageLoader.diskCache?.openSnapshot(art.url)
        if (cache == null) {
            logger.d { "image not found in cache, Downloading image" }
            val bytes = downloadImageUseCase(art.url) { downloaded, total ->
                total?.let {
                    val progress = (downloaded * 100 / total).toInt()
                    state = state.copy(downloadProgress = progress)
                }
            }.toByteArray()
            saveFile(
                bytes = bytes,
                baseName = art.title ?: "image",
                onFailure = {
                    state = state.copy(isDownloading = false)
                    scope.launch {
                        showSavingFailedMessage()
                    }
                },
                onSuccess = {
                    state = state.copy(isDownloading = false)
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
                    onFailure = {
                        state = state.copy(isDownloading = false)
                        scope.launch {
                            showSavingFailedMessage()
                        }
                    },
                    onSuccess = {
                        state = state.copy(isDownloading = false)
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
            permissionsController.performPermissionedAction(Permission.STORAGE) { pState ->
                if (pState == PermissionState.Granted) {
                    block()
                } else {
                    state = state.copy(isDownloading = false)
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
        extension: String = "jpg", // TODO get extension from file metadata
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