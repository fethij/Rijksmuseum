package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response shape for the Rijksmuseum Search API
 * (https://data.rijksmuseum.nl/search/collection), per the Linked Art Search spec.
 *
 * Pagination: follow [next].id as a full URL to fetch the next page.
 */
@Serializable
data class LinkedArtSearchResponse(
    @SerialName("orderedItems") val orderedItems: List<OrderedItem> = emptyList(),
    @SerialName("next") val next: PageRef? = null,
)

@Serializable
data class OrderedItem(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
)

@Serializable
data class PageRef(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String? = null,
)
