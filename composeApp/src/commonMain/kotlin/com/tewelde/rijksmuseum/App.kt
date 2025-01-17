package com.tewelde.rijksmuseum

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import com.tewelde.rijksmuseum.navigation.RijksmuseumNavGraph
import com.tewelde.rijksmuseum.theme.RijksmuseumTheme
import okio.FileSystem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@Composable
@Preview
fun App(
    circuit: Circuit,
    backStack: SaveableBackStack,
    navigator: Navigator,
    onRootPop: () -> Unit,
    disableDiskCache: Boolean = false,
) {
    DevelopmentEntryPoint {
        CircuitCompositionLocals(circuit) {
            RijksmuseumTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
//                    val backStack = remember { RijksmuseumNavGraph.backStack }
//                    val navigator = remember { RijksmuseumNavGraph.navigator(onRootPop) }
//            KoinContext {
//                setSingletonImageLoaderFactory { context ->
//                    if (disableDiskCache) context.asyncImageLoader() else
//                        context.asyncImageLoader().enableDiskCache()
//                }
//                RijksmuseumNavGraph(snackbarHostState = remember { SnackbarHostState() })
//            }
                    ContentWithOverlays {
                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                            decoration = GestureNavigationDecoration(onBackInvoked = navigator::pop),
                        )
                    }
                }
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