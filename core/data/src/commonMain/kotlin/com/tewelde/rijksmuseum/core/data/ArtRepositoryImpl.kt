package com.tewelde.rijksmuseum.core.data

import com.tewelde.rijksmuseum.core.common.Result
import com.tewelde.rijksmuseum.core.common.Result.Error
import com.tewelde.rijksmuseum.core.common.Result.Success
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.model.NetworkArt
import com.tewelde.rijksmuseum.core.network.model.asArtObject
import io.ktor.utils.io.ByteReadChannel

/**
 * Network backed implementation of the [ArtRepository].
 * @param rijksmuseumDataSource The data source for the arts.
 */
class ArtRepositoryImpl(
    private val rijksmuseumDataSource: RijksMuseumNetworkDataSource
) : ArtRepository {

    override suspend fun getCollection(page: Int): Result<List<Art>> =
        try {
            val collection = rijksmuseumDataSource.getCollection(page)
            Success(
                collection.filter {
                    it.networkWebImage != null
                }.map(NetworkArt::asArtObject)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Error(e)
        }

    override suspend fun getArt(objectId: String): Result<ArtObject> =
        try {
            val art = rijksmuseumDataSource.getDetail(objectId)
            Success(art.asArtObject())
        } catch (e: Exception) {
            e.printStackTrace()
            Error(e)
        }

    override suspend fun downloadImage(url: String, onDownload: (Long, Long?) -> Unit): ByteReadChannel =
        rijksmuseumDataSource.downloadImage(url, onDownload)
}