package com.tewelde.rijksmuseum.core.model

data class Art(
    val objectNumber: String,
    val title: String,
    val longTitle: String,
    val webImage: WebImage,
    val headerImage: HeaderImage?,
    val productionPlaces: List<String>
)