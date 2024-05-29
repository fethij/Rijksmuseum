package com.tewelde.rijksmuseum

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.NetworkFetcher
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.tewelde.rijksmuseum.navigation.RijksmuseumNavGraph
import com.tewelde.rijksmuseum.theme.RijksmuseumTheme
import okio.FileSystem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App(disableDiskCache: Boolean = false) {
    RijksmuseumTheme {
        KoinContext {
            val fetcher = koinInject<NetworkFetcher.Factory>()

            setSingletonImageLoaderFactory { context ->
                if (disableDiskCache) context.asyncImageLoader(fetcher) else
                    context.asyncImageLoader(fetcher).enableDiskCache()
            }

            RijksmuseumNavGraph()
        }
    }
}


/**
 * Create a new [ImageLoader] with the [PlatformContext].
 */
fun PlatformContext.asyncImageLoader(fetcher: NetworkFetcher.Factory) =
    ImageLoader
        .Builder(this)
        .components { add(fetcher) }
        .crossfade(true)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(this, 0.25)
                .strongReferencesEnabled(true)
                .build()
        }
        .logger(DebugLogger())
        .build()

/**
 * Enable disk cache for the [ImageLoader].
 */
fun ImageLoader.enableDiskCache() = this.newBuilder()
    .diskCache {
        DiskCache.Builder()
            .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
            .build()
    }.build()