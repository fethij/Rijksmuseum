package com.tewelde.rijksmuseum.feature.arts.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumError
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumLoading
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumTopBar
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailUiState
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.detail_screen
import com.tewelde.rijksmuseum.resources.lugrasimo_regular
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailScreenRoute(
    objectId: String,
    viewModel: DetailViewModel,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    DetailScreen(uiState) {
        onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.getDetail(objectId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .background(Color.Red)
            .testTag(stringResource(Res.string.detail_screen)),
        topBar = {
            RijksmuseumTopBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* TODO */ },
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        when (uiState) {
            is DetailUiState.Loading -> {
                RijksmuseumLoading()
            }

            is DetailUiState.Error -> {
                RijksmuseumError(
                    modifier = modifier.fillMaxSize(),
                )
            }

            is DetailUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(36.dp)

                ) {
                    Text(
                        text = uiState.art.title,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(
                            Font(
                                Res.font.lugrasimo_regular,
                                weight = FontWeight.Normal
                            )
                        ),
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    )
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(36.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "--",
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                        )
                        Text(text = uiState.art.description)
                    }
                    RijksmuseumImage(
                        imageUrl = uiState.art.url,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }
    }
}