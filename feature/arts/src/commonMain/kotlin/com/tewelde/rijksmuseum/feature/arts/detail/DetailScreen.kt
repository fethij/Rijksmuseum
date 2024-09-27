package com.tewelde.rijksmuseum.feature.arts.detail

import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.LocalPlatformContext
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumError
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumLoading
import com.tewelde.rijksmuseum.feature.arts.components.ArtDetail
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailEvent
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailState
import com.tewelde.rijksmuseum.feature.arts.detail.model.State
import com.tewelde.rijksmuseum.feature.arts.gallery.components.BackButton
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import com.tewelde.rijksmuseum.resources.saving_failed
import com.tewelde.rijksmuseum.resources.saving_success
import com.tewelde.rijksmuseum.resources.settings
import org.jetbrains.compose.resources.getString
import screenHeight

@Composable
fun DetailScreenRoute(
    objectId: String,
    viewModel: DetailViewModel,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration) -> Boolean,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    LaunchedEffect(Unit) {
        viewModel.onEvent(DetailEvent.LoadDetail(objectId))
    }

    DetailScreen(uiState, snackbarHostState, onShowSnackbar) {
        when (it) {
            is DetailEvent.OnDownloadClick,
            is DetailEvent.NavigateToSettings,
            is DetailEvent.PermissionErrorMessageConsumed,
            is DetailEvent.SaveFailureMessageConsumed,
            is DetailEvent.SaveSuccessMessageConsumed,
            is DetailEvent.LoadDetail -> {
                viewModel.onEvent(it)
            }

            DetailEvent.OnBackClick -> {
                onBackClick()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailState,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: suspend (String, String?, SnackbarDuration) -> Boolean,
    modifier: Modifier = Modifier,
    onEvent: (DetailEvent) -> Unit
) {
    val pfContext = LocalPlatformContext.current
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
            confirmValueChange = { sheetValue ->
                sheetValue != SheetValue.Hidden
            }
        ),
        snackbarHostState = snackbarHostState
    )

    LaunchedEffect(uiState.showPermissionError) {
        if (uiState.showPermissionError) {
            onShowSnackbar(
                getString(Res.string.permission_denied),
                getString(Res.string.settings),
                SnackbarDuration.Long
            ).run {
                if (this) onEvent(DetailEvent.NavigateToSettings)
            }
            onEvent(DetailEvent.PermissionErrorMessageConsumed)
        }
    }

    LaunchedEffect(uiState.showSavingFailedMessage) {
        if (uiState.showSavingFailedMessage) {
            onShowSnackbar(
                getString(Res.string.saving_failed),
                null,
                SnackbarDuration.Short
            )
            onEvent(DetailEvent.SaveFailureMessageConsumed)
        }
    }

    LaunchedEffect(uiState.showSavingSuccessMessage) {
        if (uiState.showSavingSuccessMessage) {
            onShowSnackbar(
                getString(Res.string.saving_success),
                null,
                SnackbarDuration.Short
            )
            onEvent(DetailEvent.SaveSuccessMessageConsumed)
        }
    }

    LaunchedEffect(uiState.state) {
        when (uiState.state) {
            is State.Success -> {
                sheetState.bottomSheetState.partialExpand()
            }

            else -> Unit
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetShape =
        MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(4),
            topEnd = CornerSize(4),
            bottomEnd = CornerSize(0),
            bottomStart = CornerSize(0)
        ),
        sheetSwipeEnabled = true,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.onSurface,
        sheetDragHandle = {
            Surface(
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                shape = CircleShape
            ) {
                Box(modifier = Modifier.size(height = 4.dp, width = 32.dp))
            }
        },
        sheetPeekHeight = if (uiState.state is State.Success) 190.dp else 0.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        sheetTonalElevation = 0.dp,
        sheetContent = {
            when (uiState.state) {
                is State.Loading,
                is State.Error -> Unit

                is State.Success -> {
                    Box(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()

                    ) {
                        var fav by remember { mutableStateOf(false) } // TODO implement room
                        ArtDetail(
                            art = uiState.state.art,
                            color = uiState.state.art.colors?.firstOrNull()?.color()
                                ?: MaterialTheme.colorScheme.primary,
                            isDownloading = uiState.isDownloading,
                            downloadProgress = uiState.downloadProgress,
                            isFavourite = fav,
                            onSetFavourite = { fav = true },
                            onRemoveFavourite = { fav = false },
                            onDownloadClicked = {
                                onEvent(DetailEvent.OnDownloadClick(pfContext))
                            },
                            onMaker = {},
                        )
                    }
                }
            }
        }
    ) {
        when (uiState.state) {
            is State.Loading -> {
                RijksmuseumLoading()
            }

            is State.Error -> {
                RijksmuseumError(
                    modifier = modifier.fillMaxSize(),
                )
            }

            is State.Success -> {
                if (sheetState.bottomSheetState.isVisible) {
                    val heightPixels = screenHeight()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(
                                ((sheetState.bottomSheetState.requireOffset() + 130) / (heightPixels))
                                    .let { if (it == 0f) 1f else it }
                            )
                    ) {
                        RijksmuseumImage(
                            imageUrl = uiState.state.art.url,
                            modifier = modifier
                                .fillMaxSize(),
                            alignment = Alignment.TopCenter,
                        )
                    }
                }
            }
        }
        BackButton(
            modifier = Modifier.statusBarsPadding()
        ) { onEvent(DetailEvent.OnBackClick) }
    }
}

/**
 * @return the first letter of first and last word in a string
 */
fun String.initials(): String =
    this.trim()
        .split(" ")
        .mapNotNull { it.firstOrNull() }.joinToString(separator = "")
        .getFirstAndLast()
        .uppercase()

fun String.getFirstAndLast() = buildAnnotatedString {
    append(this@getFirstAndLast.first())
    append(this@getFirstAndLast.last())
}.toString()

/**
 * convert hex color [String] #000000 to [Color]
 */
@ColorInt
fun String.color(): Color {
    if ((this.length != 7 && this.length != 9) || !this.startsWith("#")) {
        return Color.Transparent // Handle invalid format
    }

    val hexValue = this.substring(1).toLongOrNull(16) ?: return Color.Transparent

    val alpha = if (this.length == 9) (hexValue shr 24 and 0xFF).toInt() else 255
    val red = (hexValue shr 16 and 0xFF).toInt()
    val green = (hexValue shr 8 and 0xFF).toInt()
    val blue = (hexValue and 0xFF).toInt()

    return Color(red, green, blue, alpha)
}