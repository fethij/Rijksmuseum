package com.tewelde.rijksmuseum.feature.arts.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumLoading
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.HeaderImage
import com.tewelde.rijksmuseum.core.model.WebImage
import com.tewelde.rijksmuseum.feature.arts.gallery.components.ImageDescription
import com.tewelde.rijksmuseum.feature.arts.gallery.model.ArtsUiState
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_loading
import com.tewelde.rijksmuseum.resources.arts_screen
import com.tewelde.rijksmuseum.resources.no_arts
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArtsScreenRoute(
    viewModel: GalleryViewModel,
    onNavigateToCollection: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArtsScreen(
        uiState = uiState,
        onNavigateToCollection = onNavigateToCollection
    )

}

@Composable
internal fun ArtsScreen(
    uiState: ArtsUiState,
    modifier: Modifier = Modifier,
    onNavigateToCollection: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .testTag(stringResource(Res.string.arts_screen)),
    ) { contentPadding ->
        when (uiState) {
            is ArtsUiState.Loading -> {
                RijksmuseumLoading(
                    modifier = Modifier
                        .testTag(stringResource(Res.string.arts_loading))
                )
            }

            is ArtsUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.no_arts),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            is ArtsUiState.Success -> {
                val pagerState = rememberPagerState(
                    pageCount = { uiState.arts.size },
                    initialPage = 0,
                )
                val currentPage = pagerState.currentPage

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 2,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        RijksmuseumImage(
                            imageUrl = uiState.arts[it].webImage.url,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    ImageDescription(
                        art = uiState.arts[currentPage],
                        pagerState = pagerState,
                        onNavigateToCollection = onNavigateToCollection,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 60.dp)
                            .navigationBarsPadding()
                    )
                }
            }

            is ArtsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = uiState.message,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewArtsScreen() {
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
    ArtsScreen(
        uiState = success,
        onNavigateToCollection = {}
    )
}