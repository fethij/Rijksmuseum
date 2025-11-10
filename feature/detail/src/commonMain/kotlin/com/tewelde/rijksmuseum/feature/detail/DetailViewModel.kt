//package com.tewelde.rijksmuseum.feature.detail.detail
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import co.touchlab.kermit.Logger
//import coil3.PlatformContext
//import coil3.SingletonImageLoader
//import com.tewelde.rijksmuseum.core.common.Either
//import com.tewelde.rijksmuseum.core.domain.DownloadImageUseCase
//import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
//import com.tewelde.rijksmuseum.core.model.ArtObject
//import com.tewelde.rijksmuseum.core.permissions.model.Permission
//import com.tewelde.rijksmuseum.core.permissions.service.PermissionsService
//import com.tewelde.rijksmuseum.core.permissions.util.DeniedAlwaysException
//import com.tewelde.rijksmuseum.core.permissions.util.DeniedException
//import com.tewelde.rijksmuseum.feature.detail.FileUtil
//import com.tewelde.rijksmuseum.feature.detail.DetailEvent
//import com.tewelde.rijksmuseum.feature.detail.DetailUiState
//import com.tewelde.rijksmuseum.feature.detail.State
//import com.tewelde.rijksmuseum.feature.detail.web
//import io.ktor.utils.io.toByteArray
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class DetailViewModel(
//    val getArtDetail: GetArtDetailUseCase,
//    private val fileUtil: FileUtil,
//    private val permissionsService: PermissionsService,
//    private val downloadImageUseCase: DownloadImageUseCase
//) : ViewModel() {
//    val logger = Logger.withTag(this::class.simpleName!!)
//    private var _uiState = MutableStateFlow(DetailUiState())
//    val uiState: StateFlow<DetailUiState> = _uiState
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = DetailUiState()
//        )
//
//    fun onEvent(event: DetailEvent) {
//        when (event) {
//            is DetailEvent.OnSave -> {
//                checkPermission {
//                    saveImage(event.context)
//                }
//            }
//
//            is DetailEvent.LoadDetail -> {
//                getDetail(event.objectId)
//            }
//
//            is DetailEvent.NavigateToSettings -> {
//                permissionsService.openSettingPage(Permission.WRITE_STORAGE)
//            }
//
//            is DetailEvent.PermissionErrorMessageConsumed -> {
//                _uiState.update { detailsState ->
//                    detailsState.copy(showPermissionError = false)
//                }
//            }
//
//            is DetailEvent.SaveFailureMessageConsumed -> {
//                _uiState.update { detailsState ->
//                    detailsState.copy(showSavingFailedMessage = false)
//                }
//            }
//
//            is DetailEvent.SaveSuccessMessageConsumed -> {
//                _uiState.update { detailsState ->
//                    detailsState.copy(showSavingSuccessMessage = false)
//                }
//            }
//
//            DetailEvent.OnBackClick -> Unit
//        }
//    }
//
//    private fun saveImage(context: PlatformContext) = viewModelScope.launch {
//        _uiState.update { detailsState ->
//            detailsState.copy(
//                isDownloading = true
//            )
//        }
//        val imageLoader = SingletonImageLoader.get(context)
//        val cache = imageLoader.diskCache?.openSnapshot(art.url)
//        if (cache == null) {
//            logger.d { "image not found in cache, Downloading image" }
//            val bytes = downloadImageUseCase(art.url) { downloaded, total ->
//                total?.let {
//                    _uiState.update { detailsState ->
//                        detailsState.copy(
//                            downloadProgress = (downloaded * 100 / total).toInt().also {
//                                Logger.d { "#### download progress: $it" }
//                            }
//                        )
//                    }
//                }
//            }.toByteArray()
//            saveFile(
//                bytes = bytes,
//                baseName = art.title ?: "image",
//                extension = "jpg", // TODO get extension from file metadata
//                onFailure = {
//                    _uiState.update { detailsState ->
//                        detailsState.copy(
//                            isDownloading = false,
//                            showSavingFailedMessage = true
//                        )
//                    }
//
//                },
//                onSuccess = {
//                    _uiState.update { detailsState ->
//                        detailsState.copy(
//                            isDownloading = false,
//                            showSavingSuccessMessage = !web // TODO: check if image is saved or canceled before enabling this for all platforms
//                        )
//                    }
//                }
//            )
//        } else {
//            logger.d { "image found in cache" }
//            cache.use { snapshot ->
//                val data = snapshot.data
//                val content = fileUtil.filesystem()?.read(data) {
//                    readByteArray()
//                }
//
//                if (content == null) return@use
//
//                saveFile(
//                    bytes = content,
//                    baseName = art.title ?: "image",
//                    extension = "jpg", // TODO get extension from file metadata
//                    onFailure = {
//                        _uiState.update { detailsState ->
//                            detailsState.copy(
//                                isDownloading = false,
//                                showSavingFailedMessage = true
//                            )
//                        }
//
//                    },
//                    onSuccess = {
//                        _uiState.update { detailsState ->
//                            detailsState.copy(
//                                isDownloading = false,
//                                showSavingSuccessMessage = !web // TODO: check if image is saved or canceled before enabling this for all platforms
//                            )
//                        }
//                    }
//                )
//            }
//        }
//    }
//
//    private fun checkPermission(postPermissionGranted: () -> Unit) {
//        viewModelScope.launch {
//            try {
//                if (fileUtil.shouldAskStorageRuntimePermission()) {
//                    permissionsService.providePermission(Permission.WRITE_STORAGE)
//                }
//                postPermissionGranted()
//            } catch (_: DeniedAlwaysException) {
//                _uiState.update { detailsState ->
//                    detailsState.copy(isDownloading = false, showPermissionError = true)
//                }
//            } catch (_: DeniedException) {
//                _uiState.update { detailsState ->
//                    detailsState.copy(isDownloading = false, showPermissionError = true)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _uiState.update { detailsState ->
//                    detailsState.copy(isDownloading = false, showPermissionError = true)
//                }
//            }
//        }
//    }
//
//    private val art: ArtObject
//        get() = (_uiState.value.state as State.Success).art
//
//    private fun getDetail(objectId: String) = viewModelScope.launch {
//        val art = getArtDetail(objectId)
//        _uiState.update {
//            when (art) {
//                is Either.Left -> DetailUiState(state = State.Error(art.value))
//                is Either.Right -> DetailUiState(state = State.Success(art.value))
//            }
//        }
//    }
//
//    private fun saveFile(
//        bytes: ByteArray,
//        baseName: String,
//        extension: String,
//        onFailure: (Throwable) -> Unit = {},
//        onSuccess: () -> Unit = { }
//    ) = viewModelScope.launch {
//        fileUtil.saveFile(
//            bytes = bytes,
//            baseName = baseName,
//            extension = extension,
//            onFailure = { onFailure(it) },
//            onSuccess = { onSuccess() }
//        )
//    }
//}