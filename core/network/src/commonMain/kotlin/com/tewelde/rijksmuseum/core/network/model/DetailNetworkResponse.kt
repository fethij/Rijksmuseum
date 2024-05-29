package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class DetailNetworkResponse(
    @SerialName("artObject") var networkArtObject: NetworkArtObject,
)