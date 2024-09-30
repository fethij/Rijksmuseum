package com.tewelde.rijksmuseum.core.data

import co.touchlab.kermit.Logger
import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.model.NetworkArt
import com.tewelde.rijksmuseum.core.network.model.asArtObject
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Network backed implementation of the [ArtRepository].
 * @param rijksmuseumDataSource The data source for the arts.
 * @param ioSDispatcher The dispatcher for the IO operations.
 */
class ArtRepositoryImpl(
    private val rijksmuseumDataSource: RijksMuseumNetworkDataSource,
    private val ioSDispatcher: CoroutineDispatcher
) : ArtRepository {
    val log = Logger.withTag(this::class.simpleName!!)

    override suspend fun getCollection(page: Int): Either<ApiResponse, List<Art>> =
        withContext(ioSDispatcher) {
            try {
                val collection = rijksmuseumDataSource.getCollection(page)
                Either.Right(
                    collection.filter {
                        it.networkWebImage != null
                    }.map(NetworkArt::asArtObject)
                )
            } catch (e: IOException) {
                log.e(e) { "Error getting collection" }
                Either.Left(ApiResponse.IOException)
            } catch (e: Exception) {
                log.e(e) { "Error getting collection" }
                Either.Left(ApiResponse.HttpError)
            }
        }

    override suspend fun getArt(objectId: String): Either<ApiResponse, ArtObject> =
        withContext(ioSDispatcher) {
            try {
                val art = rijksmuseumDataSource.getDetail(objectId)
                Either.Right(art.asArtObject())
            } catch (e: IOException) {
                log.e(e) { "Error getting art" }
                Either.Left(ApiResponse.IOException)
            } catch (e: Exception) {
                log.e(e) { "Error getting art" }
                Either.Left(ApiResponse.HttpError)
            }
        }

    override suspend fun downloadImage(
        url: String,
        onDownload: (Long, Long?) -> Unit
    ): ByteReadChannel = withContext(ioSDispatcher) {
        rijksmuseumDataSource.downloadImage(url, onDownload)
    }
}