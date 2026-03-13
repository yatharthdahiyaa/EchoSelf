package com.echoself.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoself.app.data.model.Message
import com.echoself.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatBubble(message: Message) {
    val isUser = message.isFromUser
    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault())
        .format(Date(message.timestamp))

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically { it / 2 }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            if (!isUser) {
                // AI Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(PurpleVibrant, BlueAccent)
                            )
                        )
                        .border(1.dp, GlassBorderBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✦", fontSize = 14.sp, color = TextWhite)
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(
                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
            ) {
                // Bubble
                Box(
                    modifier = Modifier
                        .widthIn(max = 270.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart    = 20.dp, topEnd = 20.dp,
                                bottomStart = if (isUser) 20.dp else 4.dp,
                                bottomEnd   = if (isUser) 4.dp else 20.dp
                            )
                        )
                        .background(
                            if (isUser) {
                                Brush.linearGradient(
                                    listOf(
                                        PurpleVibrant.copy(alpha = 0.6f),
                                        PurpleGlow.copy(alpha = 0.5f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.10f),
                                        Color.White.copy(alpha = 0.06f)
                                    )
                                )
                            }
                        )
                        .border(
                            1.dp,
                            if (isUser) PurpleVibrant.copy(alpha = 0.4f)
                            else GlassBorder,
                            RoundedCornerShape(
                                topStart    = 20.dp, topEnd = 20.dp,
                                bottomStart = if (isUser) 20.dp else 4.dp,
                                bottomEnd   = if (isUser) 4.dp else 20.dp
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text       = message.text,
                        color      = TextWhite,
                        fontSize   = 15.sp,
                        lineHeight = 23.sp,
                        fontStyle  = if (!isUser) FontStyle.Italic else FontStyle.Normal,
                        fontWeight = if (isUser) FontWeight.Normal else FontWeight.Light
                    )
                }
                // Timestamp
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text     = timeStr,
                    color    = TextHint,
                    fontSize = 10.sp
                )
            }
        }
    }
}
