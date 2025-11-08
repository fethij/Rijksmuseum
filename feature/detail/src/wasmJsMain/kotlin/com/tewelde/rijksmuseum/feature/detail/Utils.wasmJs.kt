package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download
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
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            FileKit.download(
                bytes = bytes,
                fileName = "$baseName.$extension"
            )
            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    actual suspend fun shouldAskStorageRuntimePermission(): Boolean = false

}

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = true