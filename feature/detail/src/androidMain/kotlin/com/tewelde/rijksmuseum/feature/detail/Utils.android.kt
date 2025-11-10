package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import org.jetbrains.compose.resources.StringResource

@Composable
actual fun screenHeight(): Int = LocalView.current.resources.displayMetrics.heightPixels

@Composable
actual fun screenWidth(): Int = LocalView.current.resources.displayMetrics.widthPixels

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = false