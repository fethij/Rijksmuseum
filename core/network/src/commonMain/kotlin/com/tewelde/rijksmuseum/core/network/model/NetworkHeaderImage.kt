package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.HeaderImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHeaderImage(
    @SerialName("guid") val guid: String?,
    @SerialName("offsetPercentageX") val offsetPercentageX: Int?,
    @SerialName("offsetPercentageY") val offsetPercentageY: Int?,
    @SerialName("width") val width: Int?,
    @SerialName("height") val height: Int?,
    @SerialName("url") val url: String?
)

fun NetworkHeaderImage.asHeaderImage() = HeaderImage(
    guid = guid,
    offsetPercentageX = offsetPercentageX,
    offsetPercentageY = offsetPercentageY,
    width = width,
    height = height,
    url = url
)