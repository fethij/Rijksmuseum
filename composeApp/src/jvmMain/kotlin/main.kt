import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import di.DesktopAppComponent
import di.create

fun main() {

    val appComponent: DesktopAppComponent = DesktopAppComponent::class.create()
        .also { ComponentHolder.components += it }

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Rijksmuseum",
        ) {
            val backstack = rememberSaveableBackStack(root = ArtsScreen)
            val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }
            App(
                circuit = appComponent.circuit,
                backStack = backstack,
                navigator = navigator,
                onRootPop = { /* no-op */ },
            )
        }
    }
}