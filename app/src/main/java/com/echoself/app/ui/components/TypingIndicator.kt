package com.echoself.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.theme.*

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(start = 62.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color.White.copy(0.10f), Color.White.copy(0.06f))
                )
            )
            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("✦ ", fontSize = 10.sp, color = PurpleSoft)
        repeat(3) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "d$index")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f, targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 180)
                ), label = "s$index"
            )
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(PurpleVibrant, PurpleSoft)
                        )
                    )
            )
        }
    }
}
