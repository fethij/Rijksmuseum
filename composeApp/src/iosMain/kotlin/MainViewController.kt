import androidx.compose.ui.window.ComposeUIViewController
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import di.IosAppComponent
import di.IosUiComponent
import platform.UIKit.UIViewController

typealias RijksmuseumUiViewController = () -> UIViewController

fun MainViewController() = ComposeUIViewController(
    configure = {
        IosAppComponent.create().also { appComponent ->
            ComponentHolder.components += appComponent
            (appComponent as IosUiComponent.Factory).create().also { uiComponent ->
                ComponentHolder.components += uiComponent
            }
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