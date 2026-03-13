package com.echoself.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.components.AnimatedBackground
import com.echoself.app.ui.components.GlassCard
import com.echoself.app.ui.theme.*

@Composable
fun WelcomeScreen(onStartChat: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2500, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glow"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(2500, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "alpha"
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(40.dp))

            // Logo section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Glowing orb logo
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(glowScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        PurpleVibrant.copy(alpha = 0.3f * glowAlpha),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(PurpleVibrant, BlueAccent)
                                )
                            )
                            .border(2.dp, GlassBorderBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✦", fontSize = 32.sp, color = TextWhite)
                    }
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    text       = "EchoSelf",
                    fontSize   = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextWhite,
                    letterSpacing = (-1).sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text      = "A conversation with who\nyou're becoming.",
                    fontSize  = 16.sp,
                    color     = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Quote card
            GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 28.dp) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("✦", fontSize = 20.sp, color = PurpleVibrant)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text       = "\"The version of you 5 years from now has already lived through this. They want to talk.\"",
                        fontSize   = 15.sp,
                        color      = TextSoft,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureTag("🧠 Reflection")
                        FeatureTag("💜 Empathy")
                        FeatureTag("✨ Hope")
                    }
                }
            }

            // CTA Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick  = onStartChat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape    = RoundedCornerShape(20.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(PurpleVibrant, BlueAccent)
                                ),
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "Begin Your Journey",
                            fontSize   = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = TextWhite
                        )
                    }
                }
                Text(
                    text     = "Safe • Private • Non-diagnostic",
                    fontSize = 12.sp,
                    color    = TextHint
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FeatureTag(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = text, fontSize = 11.sp, color = TextMuted)
    }
}
