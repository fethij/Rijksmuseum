package com.tewelde.rijksmuseum.feature.detail

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
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


@Inject
@ContributesBinding(AppScope::class)
class AndroidFileUtil(private val application: Application) : FileUtil {
    override fun filesystem(): FileSystem? = FileSystem.SYSTEM

    override suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
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

                application.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mContentValues)
                    .apply {
                        this?.let {
                            application.contentResolver.openOutputStream(it)?.let { outStream ->
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

    override suspend fun shouldAskStorageRuntimePermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = false