import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.di.appModule
import okio.FileSystem
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    startKoin {
        modules(appModule)
    }
    CanvasBasedWindow("Rijksmuseum") {
        /**
         * Disable disk cache for wasm-js target to avoid UnsupportedOperationException.
         * @see [FileSystem.SYSTEM_TEMPORARY_DIRECTORY]
         */
        App(disableDiskCache = true) { it.bindToBrowserNavigation() }
    }
}
