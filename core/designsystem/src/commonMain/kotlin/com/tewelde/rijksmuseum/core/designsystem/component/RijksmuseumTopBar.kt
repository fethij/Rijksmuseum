package com.tewelde.rijksmuseum.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * AgvTopBar is a composable that displays a top app bar.
 * @param modifier Modifier to be applied to the top app bar.
 * @param title Title to be displayed.
 * @param backgroundColor Background color of the top app bar.
 * @param scrollBehavior Scroll behavior of the top app bar.
 * @param navigationIcon Navigation icon to be displayed.
 * @param actions Actions to be displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RijksmuseumTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = title,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = backgroundColor,
        ),
        navigationIcon = navigationIcon,
        actions = actions,
    )
}