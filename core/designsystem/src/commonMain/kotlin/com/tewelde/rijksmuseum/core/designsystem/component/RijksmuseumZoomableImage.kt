package com.tewelde.rijksmuseum.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.size.Precision
import com.github.panpf.zoomimage.CoilZoomAsyncImage

/**
 * [RijksmuseumZoomableImage] displays a zoomable image from a URL.
 * @param modifier Modifier to be applied to the image.
 * @param imageUrl URL of the image to be displayed.
 * @param contentDescription Description of the image for accessibility.
 */
@Composable
fun RijksmuseumZoomableImage(
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

    CoilZoomAsyncImage(
        model = request,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = alignment,
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