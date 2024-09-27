package com.tewelde.rijksmuseum.core.permissions.delegate

import co.touchlab.kermit.Logger
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import com.tewelde.rijksmuseum.core.permissions.model.PermissionState
import com.tewelde.rijksmuseum.core.permissions.util.DeniedAlwaysException
import com.tewelde.rijksmuseum.core.permissions.util.openAppSettingsPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.Foundation.NSRunLoop
import platform.Foundation.NSThread
import platform.Foundation.performBlock
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class StoragePermissionDelegate : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return when (val status = PHPhotoLibrary.authorizationStatus()) {
            PHAuthorizationStatusAuthorized -> PermissionState.GRANTED
            PHAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
            PHAuthorizationStatusDenied -> PermissionState.DENIED
            else -> error("unknown gallery authorization status $status")
        }
    }

    override suspend fun providePermission() {
        providePermission(PHPhotoLibrary.authorizationStatus())
    }

    private suspend fun providePermission(status: PHAuthorizationStatus) {
        return when (status) {
            PHAuthorizationStatusAuthorized -> return
            PHAuthorizationStatusNotDetermined -> {
                val newStatus = suspendCoroutine<PHAuthorizationStatus> { continuation ->
                    requestGalleryAccess { continuation.resume(it) }
                }
                providePermission(newStatus)
            }

            PHAuthorizationStatusDenied -> throw DeniedAlwaysException(Permission.WRITE_STORAGE)
            else -> error("unknown gallery authorization status $status")
        }
    }

    override fun openSettingPage() {
        openAppSettingsPage()
    }

    private fun requestGalleryAccess(callback: (PHAuthorizationStatus) -> Unit) {
        val addOnly = 1L
        PHPhotoLibrary.requestAuthorizationForAccessLevel(
            addOnly,
            mainContinuation { status: PHAuthorizationStatus ->
                Logger.d { "#### StoragePermissionDelegate requestGalleryAccess: status = $status" }
                callback(status)
            })
    }
}

internal inline fun <T1> mainContinuation(
    noinline block: (T1) -> Unit
): (T1) -> Unit = { arg1 ->
    if (NSThread.isMainThread()) {
        block.invoke(arg1)
    } else {
        MainRunDispatcher.run {
            block.invoke(arg1)
        }
    }
}

/**
 * Simple object made to ensure dispatching to the main looper on iOS
 */
internal object MainRunDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) =
        NSRunLoop.mainRunLoop.performBlock { block.run() }
}
