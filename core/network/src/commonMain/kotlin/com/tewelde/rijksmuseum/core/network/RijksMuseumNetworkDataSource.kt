package com.tewelde.rijksmuseum.core.network

import com.tewelde.rijksmuseum.core.network.model.NetworkArt
import com.tewelde.rijksmuseum.core.network.model.NetworkArtObject

/**
 * Interface representing network calls to Rijksmuseum API
 */
interface RijksMuseumNetworkDataSource {
    suspend fun getCollection(page: Int): List<NetworkArt>

    suspend fun getDetail(objectId: String): NetworkArtObject
}