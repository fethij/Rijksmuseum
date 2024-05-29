package com.tewelde.rijksmuseum.theme

//import androidx.compose.material.ripple.RippleAlpha
//import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.tewelde.rijksmuseum.resources.Res
import com.tewelde.rijksmuseum.resources.lugrasimo_regular
import org.jetbrains.compose.resources.Font

@Composable
internal fun AppTheme(
  appColorScheme: AppColorScheme = AppTheme.colorScheme,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = darkColorScheme(),
    typography = typography(LugrasimoFontFamily),
  ) {
    CompositionLocalProvider(
//      LocalAppColorScheme provides appColorScheme,
//      LocalRippleTheme provides AppRippleTheme
    ) {
      content()
    }
  }
}

internal object AppTheme {

  val colorScheme: AppColorScheme
    @Composable @ReadOnlyComposable get() = LocalAppColorScheme.current
}

//private object AppRippleTheme : RippleTheme {
//
//  @Composable override fun defaultColor() = AppTheme.colorScheme.tintedForeground
//
//  @Composable override fun rippleAlpha(): RippleAlpha = DefaultRippleAlpha
//}

private val LugrasimoFontFamily: FontFamily
  @Composable
  get() =
    FontFamily(
      Font(Res.font.lugrasimo_regular, weight = FontWeight.Normal)
    )
//
//internal val DefaultRippleAlpha =
//  RippleAlpha(
//    pressedAlpha = 0.16f,
//    focusedAlpha = 0.24f,
//    draggedAlpha = 0.24f,
//    hoveredAlpha = 0.08f
//  )

internal val SYSTEM_SCRIM = Color.Black.copy(alpha = 0.8f)
