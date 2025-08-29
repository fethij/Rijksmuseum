package com.tewelde.rijksmuseum.feature.arts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tewelde.rijksmuseum.feature.arts.gallery.ArtsScreenRoute
import com.tewelde.rijksmuseum.feature.arts.gallery.CollectionScreenRoute
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryViewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
@SerialName("gallery")
data object Gallery

@Serializable
@SerialName("arts")
internal data object ArtsScreen

@Serializable
@SerialName("collection")
internal data object CollectionScreen

/**
 * Defines the navigation graph for the gallery feature.
 */
fun NavGraphBuilder.galleryGraph(
    navController: NavHostController,
    onBackClick: () -> Unit,
    onArtClick: (String) -> Unit
) {
    navigation<Gallery>(
        startDestination = ArtsScreen
    ) {
        composable<ArtsScreen> { entry ->
            val viewModel = entry.sharedViewModel<GalleryViewModel>(navController)
            ArtsScreenRoute(viewModel = viewModel) { navController.navigate(CollectionScreen) }
        }

        composable<CollectionScreen> { entry ->
            val viewModel = entry.sharedViewModel<GalleryViewModel>(navController)
            CollectionScreenRoute(
                viewModel = viewModel,
                onBackClick = onBackClick,
                onArtClick = onArtClick
            )
        }
    }
}

/**
 * Returns a [ViewModel] scoped to the parent of the current [NavBackStackEntry].
 * This is useful when you want to share a ViewModel between multiple destinations in a navigation graph.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}