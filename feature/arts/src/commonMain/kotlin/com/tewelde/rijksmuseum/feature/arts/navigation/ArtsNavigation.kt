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
import com.tewelde.rijksmuseum.core.designsystem.RijksmuseumDestination
import com.tewelde.rijksmuseum.feature.arts.gallery.ArtsScreenRoute
import com.tewelde.rijksmuseum.feature.arts.gallery.CollectionScreenRoute
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * Defines the navigation graph for the gallery feature.
 */
fun NavGraphBuilder.galleryGraph(
    navController: NavHostController
) {
    navigation(
        route = RijksmuseumDestination.Gallery.route,
        startDestination = RijksmuseumDestination.ArtsScreen.route
    ) {
        composable(RijksmuseumDestination.ArtsScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<GalleryViewModel>(navController)
            ArtsScreenRoute(viewModel) {
                navController.navigate(RijksmuseumDestination.CollectionScreen.route)
            }
        }

        composable(RijksmuseumDestination.CollectionScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<GalleryViewModel>(navController)
            CollectionScreenRoute(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onArtClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
    }
}

/**
 * Returns a [ViewModel] scoped to the parent of the current [NavBackStackEntry].
 * This is useful when you want to share a ViewModel between multiple destinations in a navigation graph.
 */
@OptIn(KoinExperimentalAPI::class)
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