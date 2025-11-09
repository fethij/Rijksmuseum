import androidx.compose.ui.window.ComposeUIViewController
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import di.IosAppComponent


fun MainViewController() = ComposeUIViewController(
    configure = {
        IosAppComponent.create().also {
            ComponentHolder.components += it
        }
    }) {
    val appComponent: IosAppComponent = ComponentHolder.component()
    val backStack = rememberSaveableBackStack(ArtsScreen)
    val navigator = rememberCircuitNavigator(backStack, onRootPop = { /* no-op */ })
    App(
        circuit = appComponent.circuit,
        backStack = backStack,
        navigator = navigator,
        onRootPop = { /* no-op */ }
    )
}