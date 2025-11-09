package com.tewelde.rijksmuseum.feature.detail.model

import coil3.PlatformContext

sealed class DetailEvent {
    data object OnBackClick : DetailEvent()
    data object NavigateToSettings : DetailEvent()
    data object PermissionErrorMessageConsumed : DetailEvent()
    data object SaveFailureMessageConsumed : DetailEvent()
    class OnSave(val context: PlatformContext) : DetailEvent()
    data object SaveSuccessMessageConsumed : DetailEvent()
}