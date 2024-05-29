package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.WebImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWebImage(
    @SerialName("guid") var guid: String,
    @SerialName("offsetPercentageX") var offsetPercentageX: Int,
    @SerialName("offsetPercentageY") var offsetPercentageY: Int,
    @SerialName("width") var width: Int,
    @SerialName("height") var height: Int,
    @SerialName("url") var url: String
)

fun NetworkWebImage.asWebImage() = WebImage(
    guid = guid,
    offsetPercentageX = offsetPercentageX,
    offsetPercentageY = offsetPercentageY,
    width = width,
    height = height,
    url = url
)