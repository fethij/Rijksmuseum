package com.tewelde.rijksmuseum.core.data

import arrow.core.Either
import com.tewelde.rijksmuseum.core.common.ApiResponse
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
    suspend fun getCollection(page: Int): Either<ApiResponse, List<Art>>

    /**
     * Returns an [ArtObject] with the given [objectId].
     */
    suspend fun getArt(objectId: String): Either<ApiResponse, ArtObject>

    suspend fun downloadImage(url: String, onDownload: (Long, Long?) -> Unit): ByteReadChannel
}
