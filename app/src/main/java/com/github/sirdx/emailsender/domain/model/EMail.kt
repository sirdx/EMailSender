package com.github.sirdx.emailsender.domain.model

import android.net.Uri

data class EMail(
    val recipient: String,
    val subject: String,
    val body: String,
    val attachment: Uri?
)