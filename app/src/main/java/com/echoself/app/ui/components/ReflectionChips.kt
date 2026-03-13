package com.echoself.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.theme.*

val reflectionPrompts = listOf(
    "🌱 What's draining me lately?",
    "💭 I feel misunderstood because...",
    "🎯 My biggest fear right now is...",
    "✨ Something I'm proud of today",
    "🌊 I need help with...",
    "🔮 I wish I knew..."
)

@Composable
fun ReflectionChips(onChipClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(reflectionPrompts) { prompt ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                    .clickable { onChipClick(prompt) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text     = prompt,
                    color    = TextSoft,
                    fontSize = 12.sp
                )
            }
        }
    }
}
