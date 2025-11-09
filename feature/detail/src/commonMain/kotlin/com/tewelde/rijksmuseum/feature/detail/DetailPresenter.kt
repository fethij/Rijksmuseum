package com.tewelde.rijksmuseum.feature.detail

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
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(ArtDetailScreen::class, AppScope::class)
@Inject
class DetailPresenter(
    @Assisted val navigator: Navigator,
    @Assisted val screen: ArtDetailScreen,
    val scope: CoroutineScope,
    val getArtDetail: GetArtDetailUseCase,
    private val fileUtil: FileUtil,
    private val permissionsController: PermissionsController,
    private val downloadImageUseCase: DownloadImageUseCase
) : Presenter<DetailUiState> {
    val logger = Logger.withTag(this::class.simpleName!!)

    var showPermissionError by mutableStateOf(false)
    var showSavingFailedMessage by mutableStateOf(false)
    var showSavingSuccessMessage by mutableStateOf(false)
    var isDownloading by mutableStateOf(false)
    var downloadProgress: Int by mutableStateOf(0)
    var state by mutableStateOf(DetailUiState())

    @Composable
    override fun present(): DetailUiState {
        state = produceState(state) {
            value = when (val art = getArtDetail(screen.artId)) {
                is Either.Left -> DetailUiState(
                    state = State.Error(art.value),
                    eventSink = ::onEvent
                )

                is Either.Right -> DetailUiState(
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
                        isDownloading = false
                        showPermissionError = true
                        logger.e { "Error saving image: ${it.message}" }
                    }
                }
            }

            is DetailEvent.NavigateToSettings -> {
                permissionsController.openAppSettings()
//                permissionsService.openSettingPage(Permission.WRITE_STORAGE)
            }

            is DetailEvent.PermissionErrorMessageConsumed -> {
                showPermissionError = false
            }

            is DetailEvent.SaveFailureMessageConsumed -> {
                showSavingFailedMessage = false
            }

            is DetailEvent.SaveSuccessMessageConsumed -> {
                showSavingSuccessMessage = false
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
                    showSavingFailedMessage = true

                },
                onSuccess = {
                    isDownloading = false
                    showSavingSuccessMessage =
                        !web // TODO: check if image is saved or canceled before enabling this for all platforms
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
                        showSavingFailedMessage = true
                    },
                    onSuccess = {
                        isDownloading = false
                        showSavingSuccessMessage =
                            !web // TODO: check if image is saved or canceled before enabling this for all platforms
                    }
                )
            }
        }
    }

    suspend fun ensureStoragePermissionAndExecute(block: suspend () -> Unit) {
        if (!permissionsController.isPermissionGranted(Permission.STORAGE)) {
            permissionsController.performPermissionedAction(Permission.STORAGE) { state ->
                if (state == PermissionState.Granted) {
                    block()
                } else {
                    isDownloading = false
                    showPermissionError = true
                }
            }
        } else {
            block()
        }
    }

//    private fun checkPermission(postPermissionGranted: () -> Unit) {
//        scope.launch {
//            try {
//                if (fileUtil.shouldAskStorageRuntimePermission()) {
//                    permissionsController.providePermission(Permission.STORAGE)
//                }
//                postPermissionGranted()
//            } catch (_: Denied) {
//                isDownloading = false
//                showPermissionError = true
//            } catch (_: DeniedException) {
//                isDownloading = false
//                showPermissionError = true
//            } catch (e: Exception) {
//                e.printStackTrace()
//                isDownloading = false
//                showPermissionError = true
//            }
//        }
//    }

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
}