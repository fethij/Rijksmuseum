package com.tewelde.rijksmuseum.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.size.Precision

/**
 * [RijksmuseumImage] displays an image from a URL.
 * @param modifier Modifier to be applied to the image.
 * @param imageUrl URL of the image to be displayed.
 * @param contentDescription Description of the image for accessibility.
 */
@Composable
fun RijksmuseumImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
) {
    val request = Builder(LocalPlatformContext.current).apply {
        data(imageUrl)
        crossfade(true)
        precision(Precision.EXACT)
        memoryCacheKey(imageUrl)
        diskCacheKey(imageUrl)
    }.build()
    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = alignment,
        modifier = modifier,
        onState = { state ->
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Logger.d { "Image loading" }
                }

                is AsyncImagePainter.State.Error -> {
                    Logger.e(state.result.throwable) { "Image loading failed" }
                }

                is AsyncImagePainter.State.Success -> {
                    Logger.d { "Image loaded successfully" }
                }

                is AsyncImagePainter.State.Empty -> Unit
            }
        }
    )
}