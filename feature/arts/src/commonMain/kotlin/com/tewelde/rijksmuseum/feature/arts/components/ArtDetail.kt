package com.tewelde.rijksmuseum.feature.arts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults.filledTonalIconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.feature.arts.detail.initials
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.color
import com.tewelde.rijksmuseum.resources.description
import com.tewelde.rijksmuseum.resources.details
import com.tewelde.rijksmuseum.resources.production_places
import com.tewelde.rijksmuseum.resources.save_to_photos
import com.tewelde.rijksmuseum.resources.size
import com.tewelde.rijksmuseum.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtDetail(
    art: ArtObject,
    color: Color,
    isFavourite: Boolean,
    isDownloading: Boolean = false,
    downloadProgress: Int = 0,
    onSetFavourite: () -> Unit,
    onRemoveFavourite: () -> Unit,
    onDownloadClicked: () -> Unit,
    onMaker: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SheetProfileRow(
            maker = art.principalMaker,
            isFavourite = isFavourite,
            onSetFavourite = onSetFavourite,
            onRemoveFavourite = onRemoveFavourite,
            onMaker = onMaker
        )
        SheetActionRow(
            isDownloading = isDownloading,
            downloadProgress = downloadProgress,
            onDownloadClicked = onDownloadClicked,
        )
        SheetPhotoDetails(art, color)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductionPlaceDetailItem(places: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        places.forEach {
            SuggestionChip(
                label = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                onClick = {},
                shape = CircleShape,
                colors =
                SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                ),
                border =
                SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = MaterialTheme.colorScheme.secondary
                ),
            )
        }
    }
}

@Composable
fun PhotoDetailItem(
    title: String,
    alignment: Alignment.Vertical = Alignment.Top,
    suffix: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = alignment
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth().weight(3f)
        )
        Box(modifier = Modifier.fillMaxWidth().weight(7f)) { suffix() }
    }
}

@Composable
fun SheetActionRow(
    isDownloading: Boolean,
    downloadProgress: Int = 0,
    onDownloadClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onDownloadClicked() },
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(8.dp),
            enabled = !isDownloading
        ) {
            AnimatedVisibility(visible = isDownloading && downloadProgress > 0) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 2.dp,
                    progress = { downloadProgress / 100f }
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(Res.string.save_to_photos),
                style =
                MaterialTheme.typography.titleSmall.copy(
//                    fontFamily = getLatoRegular(),
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun SheetProfileRow(
    maker: String,
    isFavourite: Boolean,
    onSetFavourite: () -> Unit,
    onRemoveFavourite: () -> Unit,
    onMaker: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    .clickable { onMaker() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = maker.initials(),
                    modifier = Modifier.padding(12.dp),
                )
            }

            Text(
                text = maker,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        FilledTonalIconButton(
            colors = filledTonalIconButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,

                ),
            onClick = {
                if (isFavourite) {
                    onRemoveFavourite()
                } else {
                    onSetFavourite()
                }
            }
        ) {
            Crossfade(targetState = isFavourite) { isFavourite ->
                if (isFavourite) {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favorite")
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
        }
    }
}

@Composable
fun SheetPhotoDetails(art: ArtObject, color: Color) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "details",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(Res.string.details),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            art.title?.let {
                PhotoDetailItem(stringResource(Res.string.title)) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                    )
                }
            }

            art.description?.let {
                PhotoDetailItem(stringResource(Res.string.description)) {
                    ExpandableText(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                        collapsedMaxLine = 3
                    )
                }
            }

            PhotoDetailItem(stringResource(Res.string.size)) {
                Text(
                    "${art.webImage?.width} x ${art.webImage?.height}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                )
            }

            PhotoDetailItem(stringResource(Res.string.color)) {
                Box(
                    modifier =
                    Modifier.size(18.dp)
                        .background(color = color, shape = CircleShape)
                )
            }

            if (art.productionPlaces?.isNotEmpty() == true) {
                PhotoDetailItem(
                    stringResource(Res.string.production_places),
                    alignment = Alignment.CenterVertically
                ) {
                    ProductionPlaceDetailItem(art.productionPlaces?.distinct()!!)
                }
            }
        }

    }
}