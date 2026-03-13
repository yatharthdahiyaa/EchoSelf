package com.echoself.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.ui.components.*
import com.echoself.app.ui.theme.*
import com.echoself.app.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    currentMood: String = "okay",
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showChips by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty())
            listState.animateScrollToItem(uiState.messages.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {

            // ── Top Bar ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.04f))
                    .border(
                        width = 0.5.dp,
                        color = GlassBorder,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PurpleVibrant, BlueAccent)))
                            .border(1.dp, GlassBorderBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("✦", fontSize = 16.sp, color = TextWhite) }

                    Column {
                        Text("Your Future Self", color = TextWhite, fontSize = 16.sp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                Modifier.size(7.dp).clip(CircleShape)
                                    .background(MoodOkay)
                            )
                            Text("5 years wiser • Present", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                }
            }

            // ── Messages ──────────────────────────────────────
            LazyColumn(
                state   = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = 12.dp, bottom = 8.dp)
            ) {
                items(uiState.messages) { message ->
                    ChatBubble(message = message)
                }
                if (uiState.isLoading) {
                    item { TypingIndicator() }
                }
            }

            // ── Reflection Chips (only initially) ────────────
            if (showChips && uiState.messages.size <= 1) {
                ReflectionChips { prompt ->
                    inputText = prompt
                    showChips = false
                }
                Spacer(Modifier.height(8.dp))
            }

            // ── Input Bar ─────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.04f))
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(0.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    modifier      = Modifier.weight(1f),
                    placeholder   = {
                        Text("Share what's on your mind…", color = TextHint, fontSize = 14.sp)
                    },
                    shape   = RoundedCornerShape(20.dp),
                    colors  = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = PurpleVibrant.copy(0.6f),
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor   = Color.White.copy(0.07f),
                        unfocusedContainerColor = Color.White.copy(0.04f),
                        focusedTextColor  = TextWhite,
                        unfocusedTextColor = TextSoft,
                        cursorColor       = PurpleVibrant
                    ),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction      = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        if (inputText.isNotBlank() && !uiState.isLoading) {
                            viewModel.sendMessage(inputText.trim())
                            inputText = ""
                            showChips = false
                        }
                    })
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank())
                                Brush.linearGradient(listOf(PurpleVibrant, BlueAccent))
                            else Brush.linearGradient(listOf(Glass2, Glass1))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        if (inputText.isNotBlank() && !uiState.isLoading) {
                            viewModel.sendMessage(inputText.trim())
                            inputText = ""
                            showChips = false
                        }
                    }) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Rounded.Send,
                            contentDescription = "Send",
                            tint               = if (inputText.isNotBlank()) TextWhite else TextHint,
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Bottom Nav ────────────────────────────────────
            BottomNavBar(currentRoute = "chat", onNavigate = onNavigate)
        }
    }
}
