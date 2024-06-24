import androidx.compose.runtime.Composable
import com.tewelde.rijksmuseum.core.model.Art
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import io.github.vinceglb.filekit.core.PlatformFile
import okio.FileSystem

@Composable
expect fun screenHeight(): Int

expect val Art.artUrl: String

expect class FileUtil {
    fun filesystem(): FileSystem?
    suspend fun saveFile(
        bytes: ByteArray,
        baseName: String = "file",
        extension: String,
        initialDirectory: String? = null,
        platformSettings: FileKitPlatformSettings? = null,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )
    suspend fun shouldAskStorageRuntimePermission(): Boolean
}
