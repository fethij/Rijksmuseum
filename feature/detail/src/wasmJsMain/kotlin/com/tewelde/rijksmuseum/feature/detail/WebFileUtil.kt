package com.tewelde.rijksmuseum.feature.detail

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding


@Inject
@ContributesBinding(AppScope::class)
class WebFileUtil : FileUtil {
    override fun filesystem(): FileSystem? = null
    override suspend fun saveFile(
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

    override suspend fun shouldAskStorageRuntimePermission(): Boolean = false
}