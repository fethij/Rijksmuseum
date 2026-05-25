package com.tewelde.rijksmuseum.core.network

import com.tewelde.rijksmuseum.core.network.model.LinkedArtDigitalObject
import com.tewelde.rijksmuseum.core.network.model.LinkedArtHumanMadeObject
import com.tewelde.rijksmuseum.core.network.model.LinkedArtSearchResponse
import com.tewelde.rijksmuseum.core.network.model.LinkedArtVisualItem
import io.ktor.utils.io.ByteReadChannel

/**
 * Interface representing network calls to Rijksmuseum Data Services.
 */
interface RijksMuseumNetworkDataSource {

    /**
     * Search the collection. Returns a paginated list of LOD identifiers.
     * @param pageToken cursor for pagination (null for first page)
     * @param objectNumber optional object number filter for exact lookup
     */
    suspend fun searchCollection(
        pageToken: String? = null,
        objectNumber: String? = null
    ): LinkedArtSearchResponse

    /**
     * Resolve a LOD identifier to get the artwork metadata (HumanMadeObject).
     * @param lodId the full LOD identifier URL, e.g. "https://id.rijksmuseum.nl/200100988"
     */
    suspend fun getObject(lodId: String): LinkedArtHumanMadeObject

    /**
     * Resolve a VisualItem to get digital representation references.
     * @param visualItemId the full LOD identifier URL for the VisualItem
     */
    suspend fun getVisualItem(visualItemId: String): LinkedArtVisualItem

    /**
     * Resolve a DigitalObject to get the IIIF image access point.
     * @param digitalObjectId the full LOD identifier URL for the DigitalObject
     */
    suspend fun getDigitalObject(digitalObjectId: String): LinkedArtDigitalObject

    /**
     * Download an image from a URL.
     */
    suspend fun downloadImage(url: String, onDownload: (Long, Long?) -> Unit): ByteReadChannel
}