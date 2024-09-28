package com.tewelde.rijksmuseum.feature.arts.gallery.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    IconButton(
        onClick = { onBackClick() },
        modifier = modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp
            ),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}