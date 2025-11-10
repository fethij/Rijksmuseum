package com.tewelde.rijksmuseum.feature.detail

import okio.FileSystem

interface FileUtil {

    // TODO shouldn't this be the FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache"?, cause now it's Filesystem.SYSTEM
    // we are caching in in Filesystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache", see App.
    fun filesystem(): FileSystem?
    suspend fun saveFile(
        bytes: ByteArray,
        baseName: String = "file",
        extension: String,
        onFailure: (Throwable) -> Unit,
        onSuccess: () -> Unit
    )

    suspend fun shouldAskStorageRuntimePermission(): Boolean
}