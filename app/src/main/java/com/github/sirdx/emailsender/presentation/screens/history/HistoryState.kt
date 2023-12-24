package com.github.sirdx.emailsender.presentation.screens.history

import com.github.sirdx.emailsender.domain.model.EMail

data class HistoryState(
    val emails: List<EMail> = listOf()
)