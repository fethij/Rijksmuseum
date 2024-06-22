package com.tewelde.rijksmuseum.core.network

import com.tewelde.rijksmuseum.core.network.model.NetworkArt
import com.tewelde.rijksmuseum.core.network.model.NetworkArtObject

/**
 * Interface representing network calls to Rijksmuseum API
 */
interface RijksMuseumNetworkDataSource {
    suspend fun getCollection(page: Int): List<NetworkArt>

    suspend fun getDetail(objectId: String): NetworkArtObject

    // TODO search by maaker: search https://www.rijksmuseum.nl/api/nl/collection?key=qhMVwCto&involvedMaker=Rembrandt+van+Rijn
    // TODO search by color: search https://www.rijksmuseum.nl/api/nl/collection?key=qhMVwCto&f.normalized32Colors.hex=%23E5242B
    // TODO SEARCH BY TERM: search https://www.rijksmuseum.nl/api/nl/collection?key=qhMVwCto&q=Rembrandt
    // TODO search by production place: search https://www.rijksmuseum.nl/api/nl/collection?key=qhMVwCto&production.place=Amsterdam
    // TODO toppieces: search https://www.rijksmuseum.nl/api/nl/collection?key=qhMVwCto&toppieces=True
}