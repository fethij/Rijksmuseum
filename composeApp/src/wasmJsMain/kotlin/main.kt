import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import di.WebAppComponent
import di.WebUiComponent
import di.create
import kotlinx.browser.document

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalBrowserHistoryApi::class
)
fun main() {
    WebAppComponent::class.create().also { ComponentHolder.components += it }

    val uiComponent = ComponentHolder
        .component<WebUiComponent.Factory>()
        .create().also {
            ComponentHolder.components += it
        }

    ComposeViewport(document.body!!) {
        /**
         * Disable disk cache for wasm-js target to avoid UnsupportedOperationException.
         * @see [FileSystem.SYSTEM_TEMPORARY_DIRECTORY]
         */
        uiComponent.appUi.Content(
            onRootPop = { /* no op */ },
            disableDiskCache = true,
        )
    }
}
