package com.tewelde.rijksmuseum.core.navigation

import com.slack.circuit.runtime.screen.Screen
import com.tewelde.rijksmuseum.core.common.Parcelize

@Parcelize
data object ArtsScreen : RijksmuseumScreen("arts")

@Parcelize
data object CollectionScreen : RijksmuseumScreen("collection")

@Parcelize
data class ArtDetailScreen(
    val artId: String,
) : RijksmuseumScreen("art_detail") {

}

abstract class RijksmuseumScreen(
    val name: String,
) : Screen {
    open val arguments: Map<String, *>? = null
}