package com.tewelde.rijksmuseum

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import com.tewelde.rijksmuseum.di.AndroidAppComponent

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
        setContent {
            val backstack = rememberSaveableBackStack(ArtsScreen)
            val navigator = rememberCircuitNavigator(backstack)
            App(
                circuit = circuit,
                backStack = backstack,
                navigator = navigator,
                onRootPop = ::finish
            )

            EdgeToEdgeSideEffect(
                isStatusBarLight = !isSystemInDarkTheme(),
                isNavigationBarLight = !isSystemInDarkTheme()
            )
        }
    }
}

@Composable
fun EdgeToEdgeSideEffect(
    isStatusBarLight: Boolean,
    isNavigationBarLight: Boolean,
) {
    val activity = LocalActivity.current as ComponentActivity

    DisposableEffect(
        isStatusBarLight,
        isNavigationBarLight
    ) {
        activity.enableEdgeToEdge(
            statusBarStyle =
                if (isStatusBarLight) {
                    SystemBarStyle.light(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT
                    )
                } else {
                    SystemBarStyle.dark(
                        Color.TRANSPARENT
                    )
                },
            navigationBarStyle =
                if (isNavigationBarLight) {
                    SystemBarStyle.light(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT
                    )
                } else {
                    SystemBarStyle.dark(
                        Color.TRANSPARENT
                    )
                }
        )
        onDispose { }
    }
}