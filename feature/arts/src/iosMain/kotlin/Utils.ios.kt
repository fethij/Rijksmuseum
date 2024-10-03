import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.core.model.Art
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import okio.FileSystem
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Int {
    val view = LocalWindowInfo.current
    return remember { view.containerSize.height }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Int {
    val view = LocalWindowInfo.current
    return remember { view.containerSize.width }
}

actual class FileUtil {
    actual fun filesystem(): FileSystem? = FileSystem.SYSTEM

    @OptIn(
        ExperimentalForeignApi::class
    )
    actual suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        initialDirectory: String?,
        platformSettings: FileKitPlatformSettings?,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
            runCatching {
                val nsData: NSData = memScoped {
                    NSData.create(bytes = allocArrayOf(bytes), length = bytes.size.toULong())
                }
                val imageData = UIImage.imageWithData(data = nsData)
                if (imageData != null) {
                    UIImageWriteToSavedPhotosAlbum(
                        image = imageData,
                        completionTarget = null,
                        completionSelector = null,
                        contextInfo = null
                    )
                } else {
                    onFailure(NullPointerException())
                }
            }.onSuccess {
                onSuccess()
            }.onFailure {
                it.printStackTrace()
                onFailure(it)
            }
        }
    }

    actual suspend fun shouldAskStorageRuntimePermission(): Boolean = true
}

actual val Art.artUrl: String
    get() = this.webImage.url

actual val minGridSize: Int
    get() = 175