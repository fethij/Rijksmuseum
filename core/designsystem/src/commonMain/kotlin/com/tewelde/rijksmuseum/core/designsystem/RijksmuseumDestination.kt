package com.tewelde.rijksmuseum.core.designsystem

/**
 * Destinations used in the [RijksmuseumDestination].
 */
/* app module may be better suited to host the nav destinations */
sealed class RijksmuseumDestination(val route: String) {
    data object Gallery : RijksmuseumDestination("gallery")
    data object ArtsScreen : RijksmuseumDestination("arts")
    data object DetailScreen : RijksmuseumDestination("detail/{id}")
    data object CollectionScreen : RijksmuseumDestination("collection")
}