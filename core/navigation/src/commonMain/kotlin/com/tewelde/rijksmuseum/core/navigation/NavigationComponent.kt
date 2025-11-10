package com.tewelde.rijksmuseum.core.navigation

import com.tewelde.rijksmuseum.core.common.di.UiScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(UiScope::class)
interface NavigationComponent {
    val snackBarState: SnackBarState
}