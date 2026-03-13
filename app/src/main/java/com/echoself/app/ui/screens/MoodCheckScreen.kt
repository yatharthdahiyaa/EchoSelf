package com.echoself.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.components.AnimatedBackground
import com.echoself.app.ui.components.GlassCard
import com.echoself.app.ui.theme.*

data class Mood(val emoji: String, val label: String, val color: Color, val id: String)

val moods = listOf(
    Mood("😔", "Heavy",   MoodHeavy,   "heavy"),
    Mood("😟", "Anxious", MoodAnxious, "anxious"),
    Mood("😐", "Okay",    MoodOkay,    "okay"),
    Mood("😌", "Calm",    MoodCalm,    "calm"),
    Mood("✨", "Hopeful", MoodHopeful, "hopeful")
)

@Composable
fun MoodCheckScreen(onMoodSelected: (String) -> Unit) {
    var selectedMood by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text       = "How are you\nfeeling right now?",
                fontSize   = 32.sp,
                fontWeight = FontWeight.Bold,
                color      = TextWhite,
                textAlign  = TextAlign.Center,
                lineHeight = 42.sp,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text      = "Your future self is ready to listen.",
                fontSize  = 15.sp,
                color     = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                moods.forEach { mood ->
                    val isSelected = selectedMood == mood.id
                    val scale by animateFloatAsState(
                        targetValue    = if (isSelected) 1.15f else 1.0f,
                        animationSpec  = spring(dampingRatio = 0.5f),
                        label          = "mood_scale"
                    )
                    Column(
                        modifier = Modifier
                            .scale(scale)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedMood = mood.id }
                            .background(
                                if (isSelected) mood.color.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                            .border(
                                1.dp,
                                if (isSelected) mood.color else GlassBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(mood.emoji, fontSize = 32.sp)
                        Text(
                            text     = mood.label,
                            fontSize = 11.sp,
                            color    = if (isSelected) mood.color else TextMuted,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            // Continue button
            if (selectedMood != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(PurpleVibrant, BlueAccent))
                        )
                        .clickable { onMoodSelected(selectedMood!!) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "Continue to Conversation →",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextWhite
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Select how you're feeling", fontSize = 15.sp, color = TextHint)
                }
            }
        }
    }
}
