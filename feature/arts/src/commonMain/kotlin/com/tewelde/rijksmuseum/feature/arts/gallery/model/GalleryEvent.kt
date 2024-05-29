package com.tewelde.rijksmuseum.feature.arts.gallery.model

import com.tewelde.rijksmuseum.core.model.Art

sealed class GalleryEvent {
    data class ArtClicked(val art: Art) : GalleryEvent()
    data class PlaceFiltered(val place: String) : GalleryEvent()
}