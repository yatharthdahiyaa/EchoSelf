package com.echoself.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.data.model.JournalEntry
import com.echoself.app.ui.components.*
import com.echoself.app.ui.theme.*
import com.echoself.app.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JournalScreen(viewModel: JournalViewModel, onNavigate: (String) -> Unit) {
    val entries by viewModel.entries.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.04f))
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(0.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text("Reflection Journal", fontSize = 26.sp,
                        fontWeight = FontWeight.Bold, color = TextWhite)
                    Text("Your moments of growth", fontSize = 13.sp, color = TextMuted)
                }
            }

            if (entries.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    GlassCard(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        cornerRadius = 28.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("✦", fontSize = 40.sp)
                            Text(
                                "Your reflections will appear here after each conversation.",
                                fontSize  = 15.sp, color = TextMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(entries) { entry ->
                        JournalCard(entry = entry)
                    }
                }
            }

            BottomNavBar(currentRoute = "journal", onNavigate = onNavigate)
        }
    }
}

@Composable
private fun JournalCard(entry: JournalEntry) {
    val dateStr = SimpleDateFormat("MMMM d, yyyy • h:mm a", Locale.getDefault())
        .format(Date(entry.timestamp))

    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(entry.moodEmoji, fontSize = 20.sp)
                    Text(entry.moodLabel, fontSize = 13.sp,
                        color = PurpleSoft, fontWeight = FontWeight.Medium)
                }
                Text(dateStr, fontSize = 10.sp, color = TextHint)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "\"${entry.firstMessage}\"",
                fontSize   = 14.sp, color = TextSoft,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${entry.messageCount} exchanges • ${entry.durationMinutes} min",
                fontSize = 11.sp, color = TextHint
            )
        }
    }
}
