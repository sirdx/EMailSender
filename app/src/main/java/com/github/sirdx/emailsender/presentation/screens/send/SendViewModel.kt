package com.github.sirdx.emailsender.presentation.screens.send

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.github.sirdx.emailsender.data.EMailRepository
import com.github.sirdx.emailsender.domain.model.EMail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SendViewModel : ViewModel() {

    private val _state = MutableStateFlow(SendState())
    val state: StateFlow<SendState> = _state.asStateFlow()

    private fun validateForm() {
        _state.update { currentState ->
            var canSubmit = true

            if (currentState.recipient.isBlank()) {
                canSubmit = false
            }

            if (currentState.subject.isBlank()) {
                canSubmit = false
            }

            if (currentState.body.isBlank()) {
                canSubmit = false
            }

            currentState.copy(
                canSubmit = canSubmit
            )
        }
    }

    fun updateRecipient(recipient: String) {
        _state.update { currentState ->
            currentState.copy(
                recipient = recipient
            )
        }

        validateForm()
    }

    fun updateSubject(subject: String) {
        _state.update { currentState ->
            currentState.copy(
                subject = subject
            )
        }

        validateForm()
    }

    fun updateBody(body: String) {
        _state.update { currentState ->
            currentState.copy(
                body = body
            )
        }

        validateForm()
    }

    fun updateAttachment(attachment: Uri?) {
        _state.update { currentState ->
            currentState.copy(
                attachment = attachment
            )
        }

        validateForm()
    }

    fun saveEmail() {
        _state.update { currentState ->
            val email = EMail(
                recipient = currentState.recipient,
                subject = currentState.subject,
                body = currentState.body,
                attachment = currentState.attachment
            )

            EMailRepository.addEmail(email)

            currentState.copy(
                recipient = "",
                subject = "",
                body = "",
                attachment = null
            )
        }
    }
}