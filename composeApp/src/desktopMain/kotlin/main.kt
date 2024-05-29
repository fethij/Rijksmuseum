import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.feature.arts.di.artsModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(artsModule)
    }
    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Rijksmuseum",
        ) {
            App()
        }
    }
}