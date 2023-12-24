package com.github.sirdx.emailsender.data

import com.github.sirdx.emailsender.domain.model.EMail

object EMailRepository {

    private val _emails = mutableListOf<EMail>()

    fun addEmail(email: EMail) = _emails.add(email)

    fun getEmails() = _emails.toList()
}