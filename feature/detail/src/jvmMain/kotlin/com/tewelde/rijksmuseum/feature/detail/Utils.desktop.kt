package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import okio.FileSystem
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Int = LocalWindowInfo.current.containerSize.height

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Int = LocalWindowInfo.current.containerSize.width


actual class FileUtil {
    actual fun filesystem(): FileSystem? = FileSystem.SYSTEM
    actual suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            val file = FileKit.openFileSaver(
                suggestedName = baseName,
                extension = extension,
            )
            file?.write(bytes)
            file?.let {
                it.write(bytes)
                onSuccess()
            } ?: onFailure(Exception("File not saved"))
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    actual suspend fun shouldAskStorageRuntimePermission(): Boolean = false
}

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = false