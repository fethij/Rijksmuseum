package com.tewelde.rijksmuseum.feature.arts.detail

import FileUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.tewelde.rijksmuseum.core.domain.DownloadImageUseCase
import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import com.tewelde.rijksmuseum.core.permissions.service.PermissionsService
import com.tewelde.rijksmuseum.core.permissions.util.DeniedAlwaysException
import com.tewelde.rijksmuseum.core.permissions.util.DeniedException
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailEvent
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailState
import com.tewelde.rijksmuseum.feature.arts.detail.model.State
import io.ktor.util.toByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    val getArtDetail: GetArtDetailUseCase,
    private val fileUtil: FileUtil,
    private val permissionsService: PermissionsService,
    private val downloadImageUseCase: DownloadImageUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailState()
        )

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnDownloadClick -> {
                checkPermission {
                    downloadImage(event.context)
                }
            }

            is DetailEvent.LoadDetail -> {
                getDetail(event.objectId)
            }

            is DetailEvent.NavigateToSettings -> {
                permissionsService.openSettingPage(Permission.WRITE_STORAGE)
            }

            is DetailEvent.PermissionErrorMessageConsumed -> {
                _uiState.update { detailsState ->
                    detailsState.copy(showPermissionError = false)
                }
            }

            is DetailEvent.SaveFailureMessageConsumed -> {
                _uiState.update { detailsState ->
                    detailsState.copy(showSavingFailedMessage = false)
                }
            }

            is DetailEvent.SaveSuccessMessageConsumed -> {
                _uiState.update { detailsState ->
                    detailsState.copy(showSavingSuccessMessage = false)
                }
            }

            DetailEvent.OnBackClick -> Unit
        }
    }

    private fun downloadImage(context: PlatformContext) = viewModelScope.launch {
        _uiState.update { detailsState ->
            detailsState.copy(
                isDownloading = true
            )
        }
        val imageLoader = SingletonImageLoader.get(context)
        val cache = imageLoader.diskCache?.openSnapshot(art.url)
        if (cache == null) {
            val bytes = downloadImageUseCase(art.url) { downloaded, total ->
                total?.let {
                    _uiState.update { detailsState ->
                        detailsState.copy(
                            downloadProgress = (downloaded * 100 / total).toInt()
                        )
                    }
                }
            }.toByteArray()
            saveFile(
                bytes = bytes,
                baseName = art.title ?: "image",
                extension = "jpg", // TODO get extension from file metadata
                onFailure = {
                    _uiState.update { detailsState ->
                        detailsState.copy(
                            isDownloading = false,
                            showSavingFailedMessage = true
                        )
                    }

                },
                onSuccess = {
                    _uiState.update { detailsState ->
                        detailsState.copy(
                            isDownloading = false,
                            showSavingSuccessMessage = true
                        )
                    }
                }
            )
        } else {
            cache.use { snapshot ->
                val data = snapshot.data
                val content = fileUtil.filesystem()?.read(data) {
                    readByteArray()
                }
                if (content != null) {
                    saveFile(
                        bytes = content,
                        baseName = art.title ?: "image",
                        extension = "jpg", // TODO get extension from file metadata
                        onFailure = {
                            _uiState.update { detailsState ->
                                detailsState.copy(
                                    isDownloading = false,
                                    showSavingFailedMessage = true
                                )
                            }

                        },
                        onSuccess = {
                            _uiState.update { detailsState ->
                                detailsState.copy(
                                    isDownloading = false,
                                    showSavingSuccessMessage = true
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun checkPermission(postPermissionGranted: () -> Unit) {
        viewModelScope.launch {
            try {
                if (fileUtil.shouldAskStorageRuntimePermission()) {
                    permissionsService.providePermission(Permission.WRITE_STORAGE)
                }
                postPermissionGranted()
            } catch (deniedAlways: DeniedAlwaysException) {
                _uiState.update { detailsState ->
                    detailsState.copy(isDownloading = false, showPermissionError = true)
                }
            } catch (denied: DeniedException) {
                _uiState.update { detailsState ->
                    detailsState.copy(isDownloading = false, showPermissionError = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { detailsState ->
                    detailsState.copy(isDownloading = false, showPermissionError = true)
                }
            }
        }
    }

    private val art: ArtObject
        get() = (_uiState.value.state as State.Success).art

    private fun getDetail(objectId: String) = viewModelScope.launch {
        val art = getArtDetail(objectId)
        _uiState.update {
            when (art) {
                is Either.Left -> DetailState(state = State.Error(art.value))
                is Either.Right -> DetailState(state = State.Success(art.value))
            }
        }
    }

    private fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        onFailure: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = { }
    ) = viewModelScope.launch {
        fileUtil.saveFile(
            bytes = bytes,
            baseName = baseName,
            extension = extension,
            onFailure = { onFailure(it) },
            onSuccess = { onSuccess() }
        )
    }
}