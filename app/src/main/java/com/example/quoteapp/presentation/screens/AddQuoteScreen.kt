package com.example.quoteapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.quoteapp.data.local.model.Quote
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(
    onSave: (Quote) -> Unit, onNavigateBack: (() -> Unit)? = null
) {
    val focus = LocalFocusManager.current

    var content by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var contentTouched by rememberSaveable { mutableStateOf(false) }
    var authorTouched by rememberSaveable { mutableStateOf(false) }

    val contentError = contentTouched && content.isBlank()
    val authorError = authorTouched && author.isBlank()

    val formValid = content.isNotBlank() && author.isNotBlank()

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Quote") }, navigationIcon = {
                if (onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Add, // replace with Back if you wan
                            contentDescription = "Back"
                        )
                    }
                }
            })
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (!contentTouched) contentTouched = true
                },
                label = { Text("Quote content") },
                placeholder = { Text("e.g. Stay hungry, stay foolish.") },
                isError = contentError,
                supportingText = {
                    if (contentError) Text("Content can’t be empty")
                },
                singleLine = false,
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = author,
                onValueChange = {
                    author = it
                    if (!authorTouched) authorTouched = true
                },
                label = { Text("Author") },
                placeholder = { Text("e.g. Steve Jobs") },
                isError = authorError,
                supportingText = {
                    if (authorError) Text("Author can’t be empty")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done
                )
            )

            // Optional: a small hint / helper section
            Text(
                text = if (formValid) "Ready to save — tap the + button." else "Fill both fields to enable save.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {

                    scope.launch {
                        val randomId = UUID.randomUUID().toString()
                        contentTouched = true
                        authorTouched = true
                        if (formValid) {
                            focus.clearFocus()
                            onSave(
                                Quote(
                                    content = content.trim(),
                                    author = author.trim(),
                                    id = randomId,
                                    isFavorite = false,
                                    tags = emptyList()
                                )
                            )
                            content = ""
                            author = ""
                            contentTouched = false
                            authorTouched = false
                        }

                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Save")
            }

        }
    }
}