package com.tewelde.rijksmuseum.core.data

import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import io.ktor.utils.io.ByteReadChannel

/**
 * A page of collection results with an optional cursor for the next page.
 */
data class CollectionPage(
    val arts: List<Art>,
    val nextPageToken: String?,
)

/**
 * Data layer interface for the arts.
 */
interface ArtRepository {
    /**
     * Returns a page of [Art]s. Pass [pageToken] for subsequent pages
     * (null for the first page).
     */
    suspend fun getCollection(pageToken: String? = null): Either<ApiResponse, CollectionPage>

    /**
     * Returns an [ArtObject] with the given [objectId].
     */
    suspend fun getArt(objectId: String): Either<ApiResponse, ArtObject>

    suspend fun downloadImage(url: String, onDownload: (Long, Long?) -> Unit): ByteReadChannel
}
