import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import di.WebAppComponent
import di.create
import kotlinx.browser.document

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalBrowserHistoryApi::class
)
fun main() {
    val appComponent: WebAppComponent = WebAppComponent::class.create()
        .also { ComponentHolder.components += it }

    ComposeViewport(document.body!!) {
        val backstack = rememberSaveableBackStack(root = ArtsScreen)
        val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }
        /**
         * Disable disk cache for wasm-js target to avoid UnsupportedOperationException.
         * @see [FileSystem.SYSTEM_TEMPORARY_DIRECTORY]
         */
        App(
            modifier = Modifier.fillMaxSize(),
            circuit = appComponent.circuit,
            backStack = backstack,
            navigator = navigator,
            onRootPop = { /* no op */ },
            disableDiskCache = true,
        )
    }
}
