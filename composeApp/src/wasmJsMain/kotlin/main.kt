import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryScreen
import di.WebAppComponent
import di.create
import okio.FileSystem

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appComponent: WebAppComponent = WebAppComponent::class.create()
        .also { ComponentHolder.components += it }

    CanvasBasedWindow("Rijksmuseum") {

        val backstack = rememberSaveableBackStack(root = GalleryScreen)
        val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }
        /**
         * Disable disk cache for wasm-js target to avoid UnsupportedOperationException.
         * @see [FileSystem.SYSTEM_TEMPORARY_DIRECTORY]
         */
        App(
            circuit = appComponent.circuit,
            backStack = backstack,
            navigator = navigator,
            onRootPop = { /* no op */ },
            disableDiskCache = true
        )
    }
}
