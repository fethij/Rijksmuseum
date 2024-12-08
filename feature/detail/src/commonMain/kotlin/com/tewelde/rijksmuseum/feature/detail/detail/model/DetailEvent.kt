package com.tewelde.rijksmuseum.feature.detail.detail.model

import coil3.PlatformContext

sealed class DetailEvent {
    data object OnBackClick : DetailEvent()
    data object NavigateToSettings : DetailEvent()
    data object PermissionErrorMessageConsumed : DetailEvent()
    data object SaveFailureMessageConsumed : DetailEvent()
    class OnSave(val context: PlatformContext) : DetailEvent()
    class LoadDetail(val objectId: String) : DetailEvent()
    data object SaveSuccessMessageConsumed : DetailEvent()
}