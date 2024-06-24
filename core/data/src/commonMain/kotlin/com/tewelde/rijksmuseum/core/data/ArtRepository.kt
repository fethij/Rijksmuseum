package com.tewelde.rijksmuseum.core.data

import com.tewelde.rijksmuseum.core.common.Result
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import io.ktor.utils.io.ByteReadChannel

/**
 * Data layer interface for the arts
 */
interface ArtRepository {
    /**
     * Returns a list of [Art]s.
     */
    suspend fun getCollection(page: Int): Result<List<Art>>

    /**
     * Returns an [ArtObject] with the given [objectId].
     */
    suspend fun getArt(objectId: String): Result<ArtObject>

    suspend fun downloadImage(url: String, onDownload: (Long, Long?) -> Unit): ByteReadChannel
}
