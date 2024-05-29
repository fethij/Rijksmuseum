import androidx.compose.ui.window.ComposeUIViewController
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.feature.arts.di.artsModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(artsModule)
        }
    }
) { App() }