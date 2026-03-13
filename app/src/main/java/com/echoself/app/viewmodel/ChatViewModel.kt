package com.echoself.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoself.app.data.model.Message
import com.echoself.app.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages  : List<Message> = listOf(
        Message(
            text       = "Hey… it's me. You — just a few years ahead. " +
                         "I've been waiting for this moment. What's on your mind today?",
            isFromUser = false
        )
    ),
    val isLoading : Boolean = false,
    val sessionStartTime: Long = System.currentTimeMillis()
)

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userMessage = Message(text = text.trim(), isFromUser = true)
        _uiState.value = _uiState.value.copy(
            messages  = _uiState.value.messages + userMessage,
            isLoading = true
        )
        viewModelScope.launch {
            val (responseText, emotion) = repository.sendMessageWithEmotion(text)
            val aiMessage  = Message(text = responseText, isFromUser = false, emotion = emotion)
            _uiState.value = _uiState.value.copy(
                messages  = _uiState.value.messages + aiMessage,
                isLoading = false
            )
        }
    }

    fun getSessionSummary(): Triple<Int, Int, Long> {
        val count    = _uiState.value.messages.size
        val duration = (System.currentTimeMillis() - _uiState.value.sessionStartTime) / 60000
        return Triple(count, duration.toInt(), _uiState.value.sessionStartTime)
    }
}
