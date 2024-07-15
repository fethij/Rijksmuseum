package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.ArtObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArtObject(
    @SerialName("objectNumber") var objectNumber: String,
    @SerialName("webImage") var webImage: NetworkWebImage,
    @SerialName("label") var label: Label?,
)

fun NetworkArtObject.asArtObject(): ArtObject = ArtObject(
    objectNumber = objectNumber,
    title = label?.title,
    description = label?.description ,
    url = webImage.url,
    webImage = webImage.asWebImage()
)