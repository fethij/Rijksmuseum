package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.Art
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArt(
    @SerialName("objectNumber") var objectNumber: String,
    @SerialName("title") var title: String,
    @SerialName("longTitle") var longTitle: String,
    @SerialName("webImage") var networkWebImage: NetworkWebImage?,
    @SerialName("headerImage") var networkHeaderImage: NetworkHeaderImage?,
    @SerialName("productionPlaces") var productionPlaces: List<String>
)

fun NetworkArt.asArtObject(): Art = Art(
    objectNumber = objectNumber,
    title = title,
    longTitle = longTitle,
    webImage = networkWebImage!!.asWebImage(),
    headerImage = networkHeaderImage?.asHeaderImage(),
    productionPlaces = productionPlaces
)