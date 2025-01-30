package com.tewelde.rijksmuseum

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.tewelde.rijksmuseum.navigation.RijksmuseumNavGraph
import com.tewelde.rijksmuseum.theme.RijksmuseumTheme
import okio.FileSystem
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App(disableDiskCache: Boolean = false) {
    DevelopmentEntryPoint {
        RijksmuseumTheme {
            KoinContext {
                setSingletonImageLoaderFactory { context ->
                    if (disableDiskCache) context.asyncImageLoader() else
                        context.asyncImageLoader().enableDiskCache()
                }
                RijksmuseumNavGraph(snackbarHostState = remember { SnackbarHostState() })
            }
        }
    }
}


/**
 * Create a new [ImageLoader] with the [PlatformContext].
 */
fun PlatformContext.asyncImageLoader() =
    ImageLoader
        .Builder(this)
        .components { add(KtorNetworkFetcherFactory()) }
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