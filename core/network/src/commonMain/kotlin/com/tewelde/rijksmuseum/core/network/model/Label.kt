package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Label(
    @SerialName("title") var title: String,
    @SerialName("makerLine") var makerLine: String,
    @SerialName("description") var description: String,
    @SerialName("notes") var notes: String,
    @SerialName("date") var date: String
)