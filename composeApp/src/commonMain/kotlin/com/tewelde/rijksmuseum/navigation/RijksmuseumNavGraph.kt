package com.tewelde.rijksmuseum.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tewelde.rijksmuseum.feature.arts.navigation.Gallery
import com.tewelde.rijksmuseum.feature.arts.navigation.galleryGraph
import com.tewelde.rijksmuseum.feature.detail.navigation.detailScreen
import com.tewelde.rijksmuseum.feature.detail.navigation.navigateToDetail

/**
 * Main navigation graph for the Art Gallery Viewer app.
 * @param modifier Modifier to be applied to the NavHost.
 * @param startDestination The starting destination of the NavHost.
 * @param navController The NavController to be used by the NavHost.
 */
@Composable
fun RijksmuseumNavGraph(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    startDestination: Any = Gallery,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        startDestination = startDestination,
        navController = navController,
    ) {
        galleryGraph(
            navController = navController,
            onBackClick = navController::navigateUp,
            onArtClick = { id -> navController.navigateToDetail(id) }
        )
        detailScreen(
            onBackClick = navController::navigateUp,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = { message, action, duration ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = duration,
                ) == SnackbarResult.ActionPerformed
            }
        )
    }
}