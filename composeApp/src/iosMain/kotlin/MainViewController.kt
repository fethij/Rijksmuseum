import androidx.compose.ui.window.ComposeUIViewController
import com.tewelde.rijksmuseum.App
import com.tewelde.rijksmuseum.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(appModule)
        }
    }
) { App() }