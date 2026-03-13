package com.echoself.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.echoself.app.ui.theme.*
import kotlin.math.*

@Composable
fun AnimatedBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")

    val orb1x by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(9000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb1x"
    )
    val orb1y by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(7000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb1y"
    )
    val orb2x by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(11000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb2x"
    )
    val orb2y by infiniteTransition.animateFloat(
        initialValue = 0.7f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb2y"
    )
    val orb3x by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(13000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb3x"
    )
    val orb3y by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(10000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "orb3y"
    )

    Box(modifier = modifier.fillMaxSize().background(CosmosBlack)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Orb 1 — Purple
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(OrbPurple, Color.Transparent),
                    center = Offset(size.width * orb1x, size.height * orb1y),
                    radius = size.width * 0.55f
                ),
                radius = size.width * 0.55f,
                center = Offset(size.width * orb1x, size.height * orb1y)
            )
            // Orb 2 — Blue
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(OrbBlue, Color.Transparent),
                    center = Offset(size.width * orb2x, size.height * orb2y),
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * orb2x, size.height * orb2y)
            )
            // Orb 3 — Pink
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(OrbPink, Color.Transparent),
                    center = Offset(size.width * orb3x, size.height * orb3y),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width * orb3x, size.height * orb3y)
            )
        }
    }
}
