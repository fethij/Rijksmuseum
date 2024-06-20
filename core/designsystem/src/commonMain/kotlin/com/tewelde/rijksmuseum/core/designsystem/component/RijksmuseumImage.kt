package com.tewelde.rijksmuseum.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest

/**
 * [RijksmuseumImage] displays an image from a URL.
 * @param modifier Modifier to be applied to the image.
 * @param imageUrl URL of the image to be displayed.
 * @param contentDescription Description of the image for accessibility.
 *
 */
@Composable
fun RijksmuseumImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val request = ImageRequest.Builder(LocalPlatformContext.current)
        .data(imageUrl)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .build()
    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = Alignment.Center,
        modifier = modifier
    )
}