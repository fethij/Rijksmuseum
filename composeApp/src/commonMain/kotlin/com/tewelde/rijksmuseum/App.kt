package com.tewelde.rijksmuseum

import androidx.compose.runtime.Composable
import com.tewelde.rijksmuseum.navigation.RijksmuseumNavGraph
import com.tewelde.rijksmuseum.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    AppTheme {
        KoinContext {
            RijksmuseumNavGraph()
        }
    }
}