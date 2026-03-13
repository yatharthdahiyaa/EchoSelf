package com.echoself.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.echoself.app.data.model.JournalEntry
import com.echoself.app.ui.screens.*
import com.echoself.app.ui.theme.EchoSelfTheme
import com.echoself.app.viewmodel.ChatViewModel
import com.echoself.app.viewmodel.JournalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoSelfTheme {
                val navController    = rememberNavController()
                val chatViewModel: ChatViewModel    = viewModel()
                val journalViewModel: JournalViewModel = viewModel()
                var currentMood by remember { mutableStateOf("okay") }

                NavHost(
                    navController    = navController,
                    startDestination = "welcome"
                ) {
                    composable("welcome") {
                        WelcomeScreen(onStartChat = {
                            navController.navigate("mood") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        })
                    }
                    composable("mood") {
                        MoodCheckScreen(onMoodSelected = { mood ->
                            currentMood = mood
                            navController.navigate("chat") {
                                popUpTo("mood") { inclusive = true }
                            }
                        })
                    }
                    composable("chat") {
                        ChatScreen(
                            viewModel   = chatViewModel,
                            currentMood = currentMood,
                            onNavigate  = { route ->
                                if (route == "journal") {
                                    // Save session to journal before leaving
                                    val messages = chatViewModel.uiState.value.messages
                                    val (count, dur, _) = chatViewModel.getSessionSummary()
                                    val moodObj = moods.find { it.id == currentMood } ?: moods[2]
                                    if (messages.size > 1) {
                                        journalViewModel.addEntry(
                                            JournalEntry(
                                                moodEmoji    = moodObj.emoji,
                                                moodLabel    = moodObj.label,
                                                firstMessage = messages
                                                    .firstOrNull { it.isFromUser }?.text
                                                    ?.take(80) ?: "",
                                                messageCount    = count,
                                                durationMinutes = dur
                                            )
                                        )
                                    }
                                }
                                navController.navigate(route) {
                                    popUpTo("chat") { saveState = true }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            }
                        )
                    }
                    composable("journal") {
                        JournalScreen(
                            viewModel  = journalViewModel,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            journalViewModel = journalViewModel,
                            onNavigate       = { route ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
