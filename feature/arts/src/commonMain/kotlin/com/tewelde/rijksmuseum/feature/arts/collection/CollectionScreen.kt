package com.tewelde.rijksmuseum.feature.arts.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import artUrl
import com.slack.circuit.codegen.annotations.CircuitInject
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumFilterChip
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumTopBar
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.HeaderImage
import com.tewelde.rijksmuseum.core.model.WebImage
import com.tewelde.rijksmuseum.core.navigation.CollectionScreen
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionEvent
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionUiState
import com.tewelde.rijksmuseum.feature.arts.components.ArtItem
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_screen
import minGridSize
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import software.amazon.lastmile.kotlin.inject.anvil.AppScope


@CircuitInject(CollectionScreen::class, AppScope::class)
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(
                    items = uiState.productionPlaces,
                    key = { place -> "key-$place" }
                ) { place ->
                    RijksmuseumFilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = uiState.filteredPlaces.contains(place),
                        onSelectedChange = {
                            uiState.eventSink(CollectionEvent.PlaceFiltered(place))
                        },
                        label = {
                            Text(
                                text = place,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize
                            )
                        },
                    )
                }
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minGridSize.dp)
            ) {
                items(
                    items = uiState.filteredArts,
                    key = { it.objectNumber }
                ) { art ->
                    val height = remember { heights.random().dp }
                    ArtItem(
                        url = art.artUrl,
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
        }
    }
}

@Preview
@Composable
fun PreviewCollectionScreen() {
    val state = CollectionUiState(
        listOf(
            Art(
                title = "Title",
                webImage = WebImage(
                    url = "https://lh3.googleusercontent.com/SsEIJWka3_cYRXXSE8VD3XNOgtOxoZhqW1uB6UFj78eg8gq3G4jAqL4Z_5KwA12aD7Leqp27F653aBkYkRBkEQyeKxfaZPyDx0O8CzWg=s0",
                    width = 100,
                    height = 100,
                    offsetPercentageX = 0,
                    offsetPercentageY = 0,
                    guid = "1"
                ),
                headerImage = HeaderImage(
                    url = "https://lh3.googleusercontent.com/SsEIJWka3_cYRXXSE8VD3XNOgtOxoZhqW1uB6UFj78eg8gq3G4jAqL4Z_5KwA12aD7Leqp27F653aBkYkRBkEQyeKxfaZPyDx0O8CzWg=s0",
                    width = 100,
                    height = 100,
                    offsetPercentageX = 0,
                    offsetPercentageY = 0,
                    guid = "1"
                ),
                productionPlaces = emptyList(),
                objectNumber = "1",
                longTitle = "Long Title"
            )
        ),
        emptyList(),
        {}
    )
    CollectionContent(uiState = state)
}