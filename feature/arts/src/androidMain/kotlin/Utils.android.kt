import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun screenHeight(): Int = LocalContext.current.resources.displayMetrics.heightPixels