import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.FileSystem
import org.jetbrains.compose.resources.StringResource
import kotlin.coroutines.coroutineContext

@Composable
actual fun screenHeight(): Int = LocalView.current.resources.displayMetrics.heightPixels

@Composable
actual fun screenWidth(): Int = LocalView.current.resources.displayMetrics.widthPixels


actual class FileUtil(private val context: Context) {
    actual fun filesystem(): FileSystem? = FileSystem.SYSTEM

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
                val imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val mContentValues =
                    ContentValues().apply {
                        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                        put(MediaStore.Images.Media.DISPLAY_NAME, baseName)
                    }

                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mContentValues)
                    .apply {
                        this?.let {
                            context.contentResolver.openOutputStream(it)?.let { outStream ->
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                            }
                        }
                        onSuccess()
                    }
            }.onFailure {
                it.printStackTrace()
                onFailure(it)
            }
        }
    }

    actual suspend fun shouldAskStorageRuntimePermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}

actual val Art.artUrl: String
    get() = this.webImage.url

actual val minGridSize: Int
    get() = 175

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = false