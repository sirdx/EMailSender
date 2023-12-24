package com.github.sirdx.emailsender.presentation.screens.send

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sirdx.emailsender.R
import com.github.sirdx.emailsender.common.getFileName

@Composable
fun SendScreen(
    navController: NavHostController
) {
    val viewModel = viewModel<SendViewModel>()
    val state by viewModel.state.collectAsState()

    SendScreenContent(
        state = state,
        onRecipientChange = viewModel::updateRecipient,
        onSubjectChange = viewModel::updateSubject,
        onBodyChange = viewModel::updateBody,
        onAttachmentChange = viewModel::updateAttachment,
        onSend = viewModel::saveEmail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SendScreenContent(
    state: SendState,
    onRecipientChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onAttachmentChange: (Uri?) -> Unit,
    onSend: () -> Unit
) {
    val context = LocalContext.current

    val pickAttachmentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onAttachmentChange(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = stringResource(R.string.send_header),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.recipient,
            onValueChange = onRecipientChange,
            placeholder = { Text(text = stringResource(R.string.send_recipient)) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.subject,
            onValueChange = onSubjectChange,
            placeholder = { Text(text = stringResource(R.string.send_subject)) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.body,
            onValueChange = onBodyChange,
            placeholder = { Text(text = stringResource(R.string.send_body)) },
            minLines = 8
        )
        Spacer(modifier = Modifier.height(20.dp))
        state.attachment?.let { attachment ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.send_attachment))
                Spacer(modifier = Modifier.width(5.dp))
                InputChip(
                    selected = false,
                    onClick = { onAttachmentChange(null) },
                    label = { context.getFileName(attachment)?.let { Text(text = it) } },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.attachment == null,
            onClick = {
                pickAttachmentLauncher.launch("*/*")
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.send_choose_attachment))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canSubmit,
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(state.recipient))
                    putExtra(Intent.EXTRA_SUBJECT, state.subject)
                    putExtra(Intent.EXTRA_TEXT, state.body)

                    state.attachment?.let { uri ->
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }

                    type = "message/rfc822"
                }

                context.startActivity(
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.send_email_client)
                    )
                )
                onSend()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.send_submit))
        }
    }
}