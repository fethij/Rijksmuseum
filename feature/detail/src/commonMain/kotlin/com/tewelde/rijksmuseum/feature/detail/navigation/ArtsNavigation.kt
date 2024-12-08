package com.tewelde.rijksmuseum.feature.detail.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tewelde.rijksmuseum.feature.detail.detail.DetailViewModel
import com.tewelde.rijksmuseum.feature.detail.detail.DetailScreenRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
internal class DetailScreen(val id: String)

fun NavController.navigateToDetail(
    id: String,
    navOptions: NavOptions? = null
) =
    navigate(route = DetailScreen(id), navOptions)

fun NavGraphBuilder.detailScreen(
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration) -> Boolean,
) {
    composable<DetailScreen> { entry ->
        val viewModel = koinViewModel<DetailViewModel>()
        val id = entry.toRoute<DetailScreen>().id

        DetailScreenRoute(
            objectId = id,
            viewModel = viewModel,
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            snackbarHostState = snackbarHostState
        )
    }
}