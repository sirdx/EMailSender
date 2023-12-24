package com.github.sirdx.emailsender.presentation.screens.history

import androidx.lifecycle.ViewModel
import com.github.sirdx.emailsender.data.EMailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HistoryViewModel : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    fun refreshEmails() {
        _state.update { currentState ->
            currentState.copy(
                emails = EMailRepository.getEmails()
            )
        }
    }
}