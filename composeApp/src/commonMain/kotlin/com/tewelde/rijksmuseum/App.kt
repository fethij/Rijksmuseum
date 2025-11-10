package com.tewelde.rijksmuseum

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import com.tewelde.rijksmuseum.theme.RijksmuseumTheme
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import okio.FileSystem


interface AppUi {
    @Composable
    fun Content(
        onRootPop: () -> Unit,
        modifier: Modifier = Modifier,
        disableDiskCache: Boolean = false,
    )
}

@Inject
@SingleIn(UiScope::class)
@ContributesBinding(UiScope::class)
class RijksmuseumApp(
    private val circuit: Circuit,
) : AppUi {

    @Composable
    override fun Content(
        onRootPop: () -> Unit,
        modifier: Modifier,
        disableDiskCache: Boolean,
    ) {
        setSingletonImageLoaderFactory { context ->
            if (disableDiskCache) context.asyncImageLoader() else
                context.asyncImageLoader().enableDiskCache()
        }

        val backStack = rememberSaveableBackStack(root = ArtsScreen)
        val navigator = rememberCircuitNavigator(backStack) { onRootPop() }
        CircuitCompositionLocals(circuit) {
            RijksmuseumTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ContentWithOverlays {
                        NavigableCircuitContent(
                            modifier = modifier,
                            navigator = navigator,
                            backStack = backStack,
                            decoratorFactory = remember(navigator) {
                                GestureNavigationDecorationFactory(onBackInvoked = navigator::pop)
                            }
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