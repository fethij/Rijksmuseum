import androidx.compose.ui.window.ComposeUIViewController
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import di.IosAppComponent
import di.IosUiComponent

fun MainViewController() = ComposeUIViewController(
    configure = {
        IosAppComponent.create().also { ComponentHolder.components += it }

        ComponentHolder
            .component<IosUiComponent.Factory>()
            .create()
            .also {
                ComponentHolder.components += it
            }
    }
) {
    val uiComponent = ComponentHolder.component<IosUiComponent>()
    uiComponent.appUi.Content(
        onRootPop = { /* no-op */ }
    )
}