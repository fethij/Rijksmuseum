package com.tewelde.rijksmuseum.feature.arts.gallery.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tewelde.rijksmuseum.core.designsystem.component.RijksmuseumImage

@ExperimentalMaterialApi
@Composable
fun ArtItem(
    modifier: Modifier = Modifier,
    url: String,
    shape: Shape = MaterialTheme.shapes.small,
    onArtClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    val pressed = updateTransition(targetState = isPressed, label = "Press")

    val scale by pressed.animateFloat(
        label = "Scale",
        transitionSpec = { tween(400) }
    ) {
        if (it) .98f else 1f
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            shape = shape,
            backgroundColor = MaterialTheme.colorScheme.background,
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onArtClick() },
                        onLongPress = { onLongPress() },
                        onPress = {
                            isPressed = true
                            this.tryAwaitRelease()
                            isPressed = false
                        }
                    )
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = 10.dp,
                    shape = shape
                )
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                RijksmuseumImage(
                    imageUrl = url,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.size(24.dp / 4))
    }
}