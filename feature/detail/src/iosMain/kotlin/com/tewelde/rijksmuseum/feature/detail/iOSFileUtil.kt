package com.tewelde.rijksmuseum.feature.detail

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.coroutines.coroutineContext

@Inject
@ContributesBinding(AppScope::class)
class iOSFileUtil: FileUtil {
    override fun filesystem(): FileSystem? = FileSystem.SYSTEM

    @OptIn(
        ExperimentalForeignApi::class,
        BetaInteropApi::class
    )
    override suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
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

    override suspend fun shouldAskStorageRuntimePermission(): Boolean = true
}