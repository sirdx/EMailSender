package com.github.sirdx.emailsender.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.sirdx.emailsender.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {

    data object Send : Screen("send", R.string.screen_send, Icons.Filled.Email)
    data object History : Screen("history", R.string.screen_history, Icons.Filled.List)
}