package com.tewelde.rijksmuseum.feature.arts.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumTopBar
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.navigation.CollectionScreen
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionEvent
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionUiState
import com.tewelde.rijksmuseum.feature.arts.components.ArtItem
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_screen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@CircuitInject(CollectionScreen::class, UiScope::class)
@Composable
fun CollectionScreen(
    uiState: CollectionUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .testTag(stringResource(Res.string.arts_screen)),
    ) { contentPadding ->
        CollectionContent(uiState = uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionContent(
    uiState: CollectionUiState,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyStaggeredGridState()

    // Trigger LoadMore when near the bottom
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItems - 6
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && uiState.hasMore && !uiState.isLoadingMore) {
            uiState.eventSink(CollectionEvent.LoadMore)
        }
    }

    Scaffold(
        modifier = modifier
            .testTag(stringResource(Res.string.arts_screen)),
        topBar = {
            RijksmuseumTopBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { uiState.eventSink(CollectionEvent.OnBackClicked) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        val heights = listOf(415, 315, 375, 213, 275, 290)
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            LazyVerticalStaggeredGrid(
                state = gridState,
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = uiState.arts,
                    key = { it.objectNumber }
                ) { art ->
                    val height = remember { heights.random().dp }
                    ArtItem(
                        url = art.imageUrl,
                        onArtClick = {
                            uiState.eventSink(
                                CollectionEvent.OnNavigateToArtDetail(art.objectNumber)
                            )
                        },
                        onLongPress = { },
                        modifier = Modifier
                            .height(height)
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }

            if (uiState.isLoadingMore) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCollectionScreen() {
    val state = CollectionUiState(
        listOf(
            Art(
                objectNumber = "SK-C-5",
                title = "The Night Watch",
                imageUrl = "https://iiif.micr.io/RFwqO/full/max/0/default.jpg",
                maker = "Rembrandt van Rijn",
            )
        ),
        eventSink = {}
    )
    CollectionContent(uiState = state)
}