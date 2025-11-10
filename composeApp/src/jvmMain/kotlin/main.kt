import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import dev.zacsweers.metro.createGraph
import di.DesktopAppComponent
import di.DesktopUiComponent

fun main() {
    createGraph<DesktopAppComponent>().also {
        ComponentHolder.components += it
    }

    val uiComponent: DesktopUiComponent = ComponentHolder
        .component<DesktopUiComponent.Factory>()
        .create().also {
            ComponentHolder.components += it
        }

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Rijksmuseum",
        ) {
            uiComponent.appUi.Content(
                onRootPop = { /* no-op */ },
            )
        }
    }
}