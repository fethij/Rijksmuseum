import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import di.DesktopAppComponent
import di.DesktopUiComponent
import di.create

fun main() {
    DesktopAppComponent::class.create().also {
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