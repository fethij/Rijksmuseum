package com.tewelde.rijksmuseum.feature.arts.gallery.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage

@Composable
fun ArtItem(
    url: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
        ) {
            RijksmuseumImage(
                imageUrl = url,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = title,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp),
            )
        }
    }
}