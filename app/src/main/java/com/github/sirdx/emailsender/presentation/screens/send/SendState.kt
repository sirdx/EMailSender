package com.github.sirdx.emailsender.presentation.screens.send

import android.net.Uri

data class SendState(
    val recipient: String = "",
    val subject: String = "",
    val body: String = "",
    val attachment: Uri? = null,
    val canSubmit: Boolean = false
)