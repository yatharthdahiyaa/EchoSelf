package com.echoself.app.viewmodel

import androidx.lifecycle.ViewModel
import com.echoself.app.data.model.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JournalViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val entries: StateFlow<List<JournalEntry>> = _entries.asStateFlow()

    fun addEntry(entry: JournalEntry) {
        _entries.value = listOf(entry) + _entries.value
    }
}
