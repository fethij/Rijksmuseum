package com.tewelde.rijksmuseum.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.permission_denied
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import org.jetbrains.compose.resources.StringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenHeight(): Int = LocalWindowInfo.current.containerSize.height

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenWidth(): Int = LocalWindowInfo.current.containerSize.width

actual val permissionDeniedMessage: StringResource = Res.string.permission_denied

actual val web: Boolean
    get() = false