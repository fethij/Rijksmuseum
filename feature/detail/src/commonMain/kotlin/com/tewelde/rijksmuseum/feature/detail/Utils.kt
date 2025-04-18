package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import okio.FileSystem
import org.jetbrains.compose.resources.StringResource

@Composable
expect fun screenHeight(): Int

@Composable
expect fun screenWidth(): Int

expect val web: Boolean

expect val permissionDeniedMessage: StringResource

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
