package com.tewelde.rijksmuseum

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.di.AndroidAppComponent
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryScreen
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {

    private val appComponent: AndroidAppComponent = ComponentHolder.component()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val circuit = appComponent.circuit
        FileKit.init(this)
        setContent {
            val backstack = rememberSaveableBackStack(GalleryScreen)
            val navigator = rememberCircuitNavigator(backstack)
            App(circuit, backstack, navigator, onRootPop = ::finish)
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