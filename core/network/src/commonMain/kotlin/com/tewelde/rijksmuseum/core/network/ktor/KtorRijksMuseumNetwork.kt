package com.tewelde.rijksmuseum.core.network.ktor

import com.tewelde.rijksmuseum.core.network.RijksMuseumNetworkDataSource
import com.tewelde.rijksmuseum.core.network.model.LinkedArtDigitalObject
import com.tewelde.rijksmuseum.core.network.model.LinkedArtHumanMadeObject
import com.tewelde.rijksmuseum.core.network.model.LinkedArtSearchResponse
import com.tewelde.rijksmuseum.core.network.model.LinkedArtVisualItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.ByteReadChannel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

private const val SEARCH_URL = "https://data.rijksmuseum.nl/search/collection"
private const val LINKED_ART_JSON = "application/ld+json"

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class KtorRijksMuseumNetwork(
    private val client: HttpClient,
) : RijksMuseumNetworkDataSource {

    override suspend fun searchCollection(
        pageToken: String?,
        objectNumber: String?,
    ): LinkedArtSearchResponse {
        // pageToken is a full URL from a previous response's "next.id" — use it verbatim.
        val url = pageToken ?: SEARCH_URL
        return client.get(url) {
            header(HttpHeaders.Accept, LINKED_ART_JSON)
            if (pageToken == null) {
                url {
                    if (objectNumber != null) {
                        parameters.append("objectNumber", objectNumber)
                    } else {
                        parameters.append("imageAvailable", "true")
                    }
                }
            }
        }.body()
    }

    override suspend fun getObject(lodId: String): LinkedArtHumanMadeObject =
        client.get(lodId) { header(HttpHeaders.Accept, LINKED_ART_JSON) }.body()

    override suspend fun getVisualItem(visualItemId: String): LinkedArtVisualItem =
        client.get(visualItemId) { header(HttpHeaders.Accept, LINKED_ART_JSON) }.body()

    override suspend fun getDigitalObject(digitalObjectId: String): LinkedArtDigitalObject =
        client.get(digitalObjectId) { header(HttpHeaders.Accept, LINKED_ART_JSON) }.body()

    override suspend fun downloadImage(
        url: String,
        onDownload: (Long, Long?) -> Unit,
    ): ByteReadChannel = client.get(url) {
        onDownload { bytesSentTotal, contentLength ->
            onDownload(bytesSentTotal, contentLength)
        }
    }.bodyAsChannel()
}
