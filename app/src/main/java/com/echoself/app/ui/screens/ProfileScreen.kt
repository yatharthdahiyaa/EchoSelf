package com.echoself.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.components.*
import com.echoself.app.ui.theme.*
import com.echoself.app.viewmodel.JournalViewModel

@Composable
fun ProfileScreen(journalViewModel: JournalViewModel, onNavigate: (String) -> Unit) {
    val entries by journalViewModel.entries.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // Profile card
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 28.dp) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(PurpleVibrant, BlueAccent)))
                                .border(2.dp, GlassBorderBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("✦", fontSize = 28.sp, color = TextWhite) }

                        Spacer(Modifier.height(16.dp))
                        Text("Your Future Self", fontSize = 22.sp,
                            fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("5 years wiser • Always present",
                            fontSize = 13.sp, color = TextMuted)
                    }
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value    = "${entries.size}",
                        label    = "Reflections",
                        icon     = Icons.Rounded.Book
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value    = "${entries.sumOf { it.messageCount }}",
                        label    = "Exchanges",
                        icon     = Icons.Rounded.Chat
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value    = "${entries.size}",
                        label    = "Day Streak",
                        icon     = Icons.Rounded.LocalFireDepartment
                    )
                }

                // Affirmation card
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, null,
                                tint = GoldAccent, modifier = Modifier.size(18.dp))
                            Text("Today's Reminder", fontSize = 13.sp,
                                color = GoldAccent, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "\"Every hard moment you're sitting with right now is building the version of you I am today.\"",
                            fontSize   = 14.sp, color = TextSoft,
                            lineHeight = 22.sp, fontWeight = FontWeight.Light
                        )
                    }
                }

                // Settings
                Text("Settings", fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold, color = TextWhite)

                SettingsItem(Icons.Rounded.Language, "Language", "English")
                SettingsItem(Icons.Rounded.Shield,   "Privacy Mode", "On")
                SettingsItem(Icons.Rounded.Info,     "About EchoSelf", "v1.0")
            }

            BottomNavBar(currentRoute = "profile", onNavigate = onNavigate)
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String, icon: ImageVector) {
    GlassCard(modifier = modifier, cornerRadius = 20.dp) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = PurpleVibrant, modifier = Modifier.size(22.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(label, fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
private fun SettingsItem(icon: ImageVector, label: String, value: String) {
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 16.dp) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(icon, null, tint = PurpleSoft, modifier = Modifier.size(20.dp))
                Text(label, fontSize = 14.sp, color = TextSoft)
            }
            Text(value, fontSize = 13.sp, color = TextMuted)
        }
    }
}
