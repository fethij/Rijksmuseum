package com.tewelde.rijksmuseum.core.data

import co.touchlab.kermit.Logger
import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.common.di.RijksmuseumDispatchers
import com.tewelde.rijksmuseum.core.common.di.qualifier.Named
import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.model.LinkedArtHumanMadeObject
import com.tewelde.rijksmuseum.core.network.model.asArt
import com.tewelde.rijksmuseum.core.network.model.asArtObject
import com.tewelde.rijksmuseum.core.network.model.extractVisualItemId
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

/**
 * Network backed implementation of the [ArtRepository].
 * Uses the Rijksmuseum Data Services:
 * - Search API for collection listing (cursor-based pagination)
 * - Linked Data Resolver for object metadata
 * - IIIF Image API for images
 */
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ArtRepositoryImpl(
    private val rijksmuseumDataSource: RijksMuseumNetworkDataSource,
    @Named(RijksmuseumDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher
) : ArtRepository {
    val log = Logger.withTag(this::class.simpleName!!)

    override suspend fun getCollection(pageToken: String?): Either<ApiResponse, CollectionPage> =
        withContext(ioDispatcher) {
            try {
                val searchResponse = rijksmuseumDataSource.searchCollection(
                    pageToken = pageToken
                )
                val lodIds = searchResponse.orderedItems.map { it.id }

                // Resolve each LOD ID in parallel to get full metadata + IIIF image URL
                val arts = supervisorScope {
                    lodIds.map { lodId ->
                        async {
                            try {
                                resolveArt(lodId)
                            } catch (e: Exception) {
                                log.w(e) { "Failed to resolve art for $lodId" }
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                Either.Right(
                    CollectionPage(
                        arts = arts,
                        nextPageToken = searchResponse.next?.id
                    )
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
        withContext(ioDispatcher) {
            try {
                val searchResponse = rijksmuseumDataSource.searchCollection(
                    objectNumber = objectId
                )
                val lodId = searchResponse.orderedItems.firstOrNull()?.id
                    ?: return@withContext Either.Left(ApiResponse.HttpError)

                val obj = rijksmuseumDataSource.getObject(lodId)
                val imageUrl = resolveImageUrl(obj)
                Either.Right(obj.asArtObject(imageUrl))
            } catch (e: IOException) {
                log.e(e) { "Error getting art" }
                Either.Left(ApiResponse.IOException)
            } catch (e: Exception) {
                log.e(e) { "Error getting art" }
                Either.Left(ApiResponse.HttpError)
            }
        }

    /**
     * Resolves a LOD identifier to an [Art] object with its IIIF image URL.
     */
    private suspend fun resolveArt(lodId: String): Art? {
        val obj = rijksmuseumDataSource.getObject(lodId)
        val imageUrl = resolveImageUrl(obj)
        if (imageUrl.isEmpty()) return null
        return obj.asArt(imageUrl)
    }

    /**
     * Resolves the IIIF image URL for a HumanMadeObject.
     * Chain: shows → VisualItem → digitally_shown_by → DigitalObject → access_point
     */
    private suspend fun resolveImageUrl(obj: LinkedArtHumanMadeObject): String {
        val visualItemId = obj.extractVisualItemId() ?: return ""

        return try {
            val visualItem = rijksmuseumDataSource.getVisualItem(visualItemId)
            val digitalObjectId = visualItem.digitallyShownBy.firstOrNull()?.id ?: return ""
            val digitalObject = rijksmuseumDataSource.getDigitalObject(digitalObjectId)
            digitalObject.accessPoint.firstOrNull()?.id ?: ""
        } catch (e: Exception) {
            log.w(e) { "Failed to resolve image URL for ${obj.id}" }
            ""
        }
    }

    override suspend fun downloadImage(
        url: String,
        onDownload: (Long, Long?) -> Unit
    ): ByteReadChannel = withContext(ioDispatcher) {
        rijksmuseumDataSource.downloadImage(url, onDownload)
    }
}