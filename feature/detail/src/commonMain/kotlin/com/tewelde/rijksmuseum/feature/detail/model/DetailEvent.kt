package com.tewelde.rijksmuseum.feature.detail.model

import coil3.PlatformContext
import com.tewelde.rijksmuseum.core.model.ArtObject

sealed class DetailEvent {
    data object OnBackClick : DetailEvent()
    data object NavigateToSettings : DetailEvent()
    class OnSave(val context: PlatformContext) : DetailEvent()
}