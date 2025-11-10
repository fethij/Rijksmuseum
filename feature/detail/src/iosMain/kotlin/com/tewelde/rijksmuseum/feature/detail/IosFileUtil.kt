package com.tewelde.rijksmuseum.feature.detail

import com.tewelde.rijksmuseum.core.common.di.RijksmuseumDispatchers
import com.tewelde.rijksmuseum.core.common.di.qualifier.Named
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class IosFileUtil(
    private val scope: CoroutineScope,
    @Named(RijksmuseumDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher
) : FileUtil {
    override fun filesystem(): FileSystem = FileSystem.SYSTEM

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
        scope.launch(ioDispatcher) {
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