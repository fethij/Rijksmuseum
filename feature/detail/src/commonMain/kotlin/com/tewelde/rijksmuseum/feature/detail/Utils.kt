package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource

@Composable
expect fun screenHeight(): Int

@Composable
expect fun screenWidth(): Int

expect val web: Boolean

expect val permissionDeniedMessage: StringResource
