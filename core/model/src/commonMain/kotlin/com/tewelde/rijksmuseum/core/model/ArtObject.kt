package com.tewelde.rijksmuseum.core.model

data class ArtObject(
    val objectNumber: String,
    val title: String?,
    val description: String?,
    val imageUrl: String,
    val principalMaker: String,
)