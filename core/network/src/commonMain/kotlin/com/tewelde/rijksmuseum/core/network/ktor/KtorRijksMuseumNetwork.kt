package com.tewelde.rijksmuseum.core.network.ktor

import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.model.CollectionNetworkResponse
import com.tewelde.rijksmuseum.core.network.model.DetailNetworkResponse
import com.tewelde.rijksmuseum.core.network.model.NetworkArt
import com.tewelde.rijksmuseum.core.network.model.NetworkArtObject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

private const val PS = "ps"
private const val PAGING_PAGE_SIZE = 100
const val RIJKSMUSEUM_HOST = "www.rijksmuseum.nl"
const val RIJKSMUSEUM_PATH = "api/en/"
const val COLLECTION = "collection"
const val PAGE = "p"
const val HAS_IMAGE = "hasImage"

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class KtorRijksMuseumNetwork(
    private val rijksmuseumClient: HttpClient,
    private val client: HttpClient, // todo named
) : RijksMuseumNetworkDataSource {
    override suspend fun getCollection(page: Int): List<NetworkArt> =
        rijksmuseumClient.get("$COLLECTION?&$PAGE=$page&$PS=$PAGING_PAGE_SIZE")
            .body<CollectionNetworkResponse>().networkArtObjects


    override suspend fun getDetail(objectId: String): NetworkArtObject =
        rijksmuseumClient.get("$COLLECTION/$objectId")
            .body<DetailNetworkResponse>().networkArtObject

    override suspend fun downloadImage(
        url: String,
        onDownload: (Long, Long?) -> Unit
    ): ByteReadChannel = client.get(url) {
            onDownload { bytesSentTotal, contentLength ->
                onDownload(bytesSentTotal, contentLength)
            }
        }.bodyAsChannel()
}