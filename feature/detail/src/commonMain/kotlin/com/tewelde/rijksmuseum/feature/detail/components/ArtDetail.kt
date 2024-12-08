package com.tewelde.rijksmuseum.feature.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.feature.detail.detail.initials
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
    isDownloading: Boolean = false,
    downloadProgress: Int = 0,
    onSave: () -> Unit,
    onMaker: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SheetProfileRow(
            maker = art.principalMaker,
            onMaker = onMaker
        )
        SheetActionRow(
            isDownloading = isDownloading,
            downloadProgress = downloadProgress,
            onSave = onSave,
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
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    labelColor = MaterialTheme.colorScheme.onBackground
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = MaterialTheme.colorScheme.onBackground,
                )
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
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onSave() },
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(8.dp),
            enabled = !isDownloading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            AnimatedVisibility(visible = isDownloading && downloadProgress > 0) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 2.dp,
                    progress = { downloadProgress / 100f })
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(Res.string.save_to_photos),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun SheetProfileRow(
    maker: String,
    onMaker: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .shadow(
                    elevation = 8.dp, shape = CircleShape
                ),
//                    .clickable { onMaker() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = maker.initials(),
                modifier = Modifier.padding(12.dp),
            )
        }

        Text(
            text = maker, style = MaterialTheme.typography.bodyLarge
        )
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
                        text = it, style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
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
                    modifier = Modifier.size(18.dp).background(color = color, shape = CircleShape)
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