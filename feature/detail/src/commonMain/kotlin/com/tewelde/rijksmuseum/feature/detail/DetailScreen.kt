package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.foundation.background
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.slack.circuit.codegen.annotations.CircuitInject
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumError
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumLoading
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumZoomableImage
import com.tewelde.rijksmuseum.core.navigation.ArtDetailScreen
import com.tewelde.rijksmuseum.core.navigation.SnackBarStateEffect
import com.tewelde.rijksmuseum.feature.detail.components.ArtDetail
import com.tewelde.rijksmuseum.feature.detail.components.BackButton
import com.tewelde.rijksmuseum.feature.detail.model.DetailEvent
import com.tewelde.rijksmuseum.feature.detail.model.DetailUiState
import com.tewelde.rijksmuseum.feature.detail.model.State
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_screen
import org.jetbrains.compose.resources.stringResource

@CircuitInject(ArtDetailScreen::class, UiScope::class)
@Composable
fun DetailScreen(
    state: DetailUiState,
    modifier: Modifier = Modifier,
) {
    DetailContent(
        uiState = state,
        modifier = modifier.testTag(stringResource(Res.string.arts_screen)),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    uiState: DetailUiState,
    modifier: Modifier = Modifier,
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
        snackbarHostState = uiState.snackbarHostState
    )

    SnackBarStateEffect()

    LaunchedEffect(uiState.state) {
        when (uiState.state) {
            is State.Success -> {
                sheetState.bottomSheetState.partialExpand()
            }

            else -> Unit
        }
    }

    val peekHeight = if (uiState.state is State.Success) 190.dp else 0.dp
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetState,
        snackbarHost = {
            // Default snackbar host is anchored to the scaffold's bottom edge, which sits
            // behind the bottom sheet's peek area. Push it above the peek so it stays visible.
            SnackbarHost(
                hostState = uiState.snackbarHostState,
                modifier = Modifier.padding(bottom = peekHeight),
            )
        },
        sheetShape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(4),
            topEnd = CornerSize(4),
            bottomEnd = CornerSize(0),
            bottomStart = CornerSize(0)
        ),
        sheetSwipeEnabled = true,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetContentColor = MaterialTheme.colorScheme.onBackground,
        sheetDragHandle = {
            Surface(
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                shape = CircleShape
            ) {
                Box(modifier = Modifier.size(height = 4.dp, width = 32.dp))
            }
        },
        sheetPeekHeight = peekHeight,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        sheetTonalElevation = 0.dp,
        sheetMaxWidth = screenWidth().dp,
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
                        ArtDetail(
                            art = uiState.state.art,
                            isDownloading = uiState.isDownloading,
                            downloadProgress = uiState.downloadProgress,
                            onSave = {
                                uiState.eventSink(DetailEvent.OnSave(pfContext))
                            },
                            onMaker = {},
                        )
                    }
                }
            }
        }
    ) {
        when (val state = uiState.state) {
            is State.Loading -> {
                RijksmuseumLoading(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                )
            }

            is State.Error -> {
                RijksmuseumError(
                    modifier = modifier.fillMaxSize(),
                    text = state.message
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
                        RijksmuseumZoomableImage(
                            modifier = Modifier.fillMaxSize(),
                            imageUrl = state.art.imageUrl
                        )
                    }
                }
            }
        }
        BackButton(
            modifier = Modifier.statusBarsPadding()
        ) { uiState.eventSink(DetailEvent.OnBackClick) }
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
    if (this@getFirstAndLast.length > 1) {
        append(this@getFirstAndLast.last())
    }
}.toString()