import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.feature.arts.di.artsModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(artsModule)
    }
    CanvasBasedWindow("Rijksmuseum") {
        App()
    }
}
