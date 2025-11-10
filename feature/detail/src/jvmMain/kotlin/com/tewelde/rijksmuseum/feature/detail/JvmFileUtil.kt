package com.tewelde.rijksmuseum.feature.detail

import com.tewelde.rijksmuseum.core.common.di.RijksmuseumDispatchers
import com.tewelde.rijksmuseum.core.common.di.qualifier.Named
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding


@Inject
@ContributesBinding(AppScope::class)
class JvmFileUtil(
    @Named(RijksmuseumDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher
) : FileUtil {
    override fun filesystem(): FileSystem = FileSystem.SYSTEM
    override suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) = withContext(ioDispatcher) {
        try {
            val file = FileKit.openFileSaver(
                suggestedName = baseName,
                extension = extension,
            )
            file?.let {
                it.write(bytes)
                onSuccess()
            } ?: onFailure(Exception("File not saved"))
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override suspend fun shouldAskStorageRuntimePermission(): Boolean = false
}