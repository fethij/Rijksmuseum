package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.ArtObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArtObject(
    @SerialName("objectNumber") var objectNumber: String,
    @SerialName("webImage") var webImage: NetworkWebImage,
    @SerialName("label") var label: Label?,
    @SerialName("principalMaker") val principalMaker: String?,
    @SerialName("colors") val colors: List<Color>?,
    @SerialName("principalMakers") val principalMakers: List<NetworkPrincipalMaker>,
    @SerialName("productionPlaces") val productionPlaces: List<String>?
)

fun NetworkArtObject.asArtObject(): ArtObject = ArtObject(
    objectNumber = objectNumber,
    title = label?.title,
    description = label?.description,
    url = webImage.url,
    webImage = webImage.asWebImage(),
    principalMaker = principalMaker ?: "Unknown",
    colors = colors?.map { it.hex },
    productionPlaces = productionPlaces
)

@Serializable
data class Color(
    @SerialName("percentage") val percentage: Int,
    @SerialName("hex") val hex: String
)