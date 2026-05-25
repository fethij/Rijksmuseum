package com.tewelde.rijksmuseum.feature.arts.arts

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumLoading
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import com.tewelde.rijksmuseum.feature.arts.arts.model.ArtsEvent
import com.tewelde.rijksmuseum.feature.arts.arts.model.ArtsUiState
import com.tewelde.rijksmuseum.feature.arts.components.ImageDescription
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.arts_loading
import com.tewelde.rijksmuseum.resources.arts_screen
import com.tewelde.rijksmuseum.resources.no_arts
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@CircuitInject(ArtsScreen::class, UiScope::class)
@Composable
fun ArtsScreen(
    uiState: ArtsUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .testTag(stringResource(Res.string.arts_screen)),
    ) { contentPadding ->
        ArtsContent(uiState = uiState)
    }
}

@Composable
internal fun ArtsContent(
    uiState: ArtsUiState,
) {
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
            val currentPage = pagerState.currentPage.coerceIn(0, (uiState.arts.size - 1).coerceAtLeast(0))

            if (uiState.arts.isEmpty()) return

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
                        imageUrl = uiState.arts[it].imageUrl,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ImageDescription(
                    art = uiState.arts[currentPage],
                    pagerState = pagerState,
                    onNavigateToCollection = { uiState.eventSink(ArtsEvent.OnNavigateToCollection) },
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

@Preview
@Composable
fun PreviewArtsScreen() {
    val success = ArtsUiState.Success(
        listOf(
            Art(
                objectNumber = "SK-C-5",
                title = "The Night Watch",
                imageUrl = "https://iiif.micr.io/RFwqO/full/max/0/default.jpg",
                maker = "Rembrandt van Rijn",
            )
        ),
        {}
    )
    ArtsContent(uiState = success)
}