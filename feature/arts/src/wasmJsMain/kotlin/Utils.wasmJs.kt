import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.resources.*
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import okio.FileSystem
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Int = LocalWindowInfo.current.containerSize.height

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Int = LocalWindowInfo.current.containerSize.width

actual class FileUtil {
    actual fun filesystem(): FileSystem? = null
    actual suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        initialDirectory: String?,
        platformSettings: FileKitPlatformSettings?,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            val file = FileKit.saveFile(
                bytes = bytes,
                baseName = baseName,
                extension = extension,
                initialDirectory = initialDirectory,
                platformSettings = platformSettings
            )
            file?.let { onSuccess() } ?: onFailure(Exception("File not saved"))
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    actual suspend fun shouldAskStorageRuntimePermission(): Boolean = false

}

actual val Art.artUrl: String
    get() = this.headerImage?.url ?: this.webImage.url

actual val minGridSize: Int
    get() = 325

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied