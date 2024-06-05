package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Label(
    @SerialName("title") var title: String?,
    @SerialName("description") var description: String?,
)