package com.echoself.app.data.model

data class JournalEntry(
    val id           : String = java.util.UUID.randomUUID().toString(),
    val timestamp    : Long   = System.currentTimeMillis(),
    val moodEmoji    : String,
    val moodLabel    : String,
    val firstMessage : String,
    val messageCount : Int,
    val durationMinutes: Int
)
