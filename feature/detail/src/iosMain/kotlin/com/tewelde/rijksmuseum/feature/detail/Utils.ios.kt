package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied_ios
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
import org.jetbrains.compose.resources.StringResource
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Int = LocalWindowInfo.current.containerSize.height

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Int = LocalWindowInfo.current.containerSize.width

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

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied_ios

actual val web: Boolean
    get() = false