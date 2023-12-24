package com.github.sirdx.emailsender.presentation.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sirdx.emailsender.R
import com.github.sirdx.emailsender.domain.model.EMail

@Composable
fun HistoryScreen(
    navController: NavHostController
) {
    val viewModel = viewModel<HistoryViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshEmails()
    }

    HistoryScreenContent(
        state = state
    )
}

@Composable
private fun HistoryScreenContent(
    state: HistoryState
) {
    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                text = stringResource(R.string.history_header),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        items(state.emails) { email ->
            EmailItem(email = email)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun EmailItem(
    email: EMail
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.history_recipient, email.recipient),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = email.subject,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                email.attachment?.let { _ ->
                    Text(
                        text = stringResource(R.string.history_attachment),
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = email.body,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}