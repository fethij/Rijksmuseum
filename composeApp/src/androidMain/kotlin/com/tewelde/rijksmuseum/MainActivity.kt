package com.tewelde.rijksmuseum

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FileKit.init(this)
        setContent {
            App()
        }
    }
}

//private fun ComponentActivity.enableEdgeToEdgeForTheme(theme: TiviPreferences.Theme) {
//    val style = when (theme) {
//        TiviPreferences.Theme.LIGHT -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
//        TiviPreferences.Theme.DARK -> SystemBarStyle.dark(Color.TRANSPARENT)
//        TiviPreferences.Theme.SYSTEM -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
//    }
//    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
//    // Fix for three-button nav not properly going edge-to-edge.
//    // TODO: https://issuetracker.google.com/issues/298296168
//    window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
//}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}