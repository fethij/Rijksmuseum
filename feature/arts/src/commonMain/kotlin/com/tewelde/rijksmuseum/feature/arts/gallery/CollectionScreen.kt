package com.tewelde.rijksmuseum.feature.arts.gallery

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumFilterChip
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumTopBar
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.HeaderImage
import com.tewelde.rijksmuseum.core.model.WebImage
import com.tewelde.rijksmuseum.feature.arts.gallery.components.ArtItem
import com.tewelde.rijksmuseum.feature.arts.gallery.model.ArtsUiState
import com.tewelde.rijksmuseum.feature.arts.gallery.model.GalleryEvent
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_screen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import artUrl

@Composable
fun CollectionScreenRoute(
    viewModel: GalleryViewModel,
    onBackClick: () -> Unit,
    onArtClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    CollectionScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onArtClick = onArtClick,
        onPlaceFiltered = viewModel::onEvent,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun CollectionScreen(
    uiState: ArtsUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onArtClick: (String) -> Unit,
    onPlaceFiltered: (GalleryEvent) -> Unit
) {
    Scaffold(
        modifier = modifier
            .testTag(stringResource(Res.string.arts_screen)),
        topBar = {
            RijksmuseumTopBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
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
        when (uiState) {
            is ArtsUiState.Success -> {
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
                                    onPlaceFiltered(
                                        GalleryEvent.PlaceFiltered(
                                            place
                                        )
                                    )
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
                        columns = StaggeredGridCells.Fixed(2)
                    ) {
                        items(
                            items = uiState.filteredArts,
                            key = { it.objectNumber }
                        ) { art ->
                            val height = remember { heights.random().dp }
                            ArtItem(
                                url = art.artUrl,
                                onArtClick = { onArtClick(art.objectNumber) },
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

            is ArtsUiState.Loading,
            is ArtsUiState.Empty -> Unit
        }
    }
}

@Preview
@Composable
fun PreviewCollectionScreen() {
    val success = ArtsUiState.Success(
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
        )
    )
    CollectionScreen(
        uiState = success,
        onBackClick = {},
        onArtClick = {},
        onPlaceFiltered = {}
    )
}