import androidx.compose.ui.ExperimentalComposeUiApi
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
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
            disableDiskCache = true,
            onNavHostReady = { it.bindToBrowserNavigation() }
        )
    }
}
