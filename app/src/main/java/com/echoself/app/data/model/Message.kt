package com.echoself.app.data.model

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val emotion: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
