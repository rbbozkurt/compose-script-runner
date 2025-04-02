package org.rbbozkurt.composescriptrunner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import composescriptrunner.composeapp.generated.resources.Res
import composescriptrunner.composeapp.generated.resources.btn_reset
import composescriptrunner.composeapp.generated.resources.btn_run_script
import composescriptrunner.composeapp.generated.resources.btn_running
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.rbbozkurt.composescriptrunner.script.ScriptExecutionResult
import org.rbbozkurt.composescriptrunner.script.runScript
import org.rbbozkurt.composescriptrunner.ui.components.EditorPane
import org.rbbozkurt.composescriptrunner.ui.components.OutputPane
import org.rbbozkurt.composescriptrunner.ui.state.OutputPaneUiState

@Composable
fun MainScreen() {
    var editorValue by remember { mutableStateOf(TextFieldValue("")) }
    var outputState by remember { mutableStateOf<OutputPaneUiState>(OutputPaneUiState.Idle) }
    var cursorPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var isRunning by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    // Handle updating the cursor position when requested.
    LaunchedEffect(cursorPosition) {
        cursorPosition?.let { (line, column) ->
            val lines = editorValue.text.lines()
            val rawOffset = lines.take(line - 1).sumOf { it.length + 1 } + (column - 1).coerceAtLeast(0)
            val offset = rawOffset.coerceIn(0, editorValue.text.length)
            editorValue = editorValue.copy(selection = TextRange(offset))
            focusRequester.requestFocus()
            cursorPosition = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Editor and Output row.
            Row(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    EditorPane(
                        fieldValue = editorValue,
                        onValueChange = { editorValue = it },
                        focusRequester = focusRequester
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    OutputPane(
                        state = outputState,
                        onNavigateToError = { line, column ->
                            cursorPosition = line to column
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Button row.
            ButtonRow(
                isEnabled = isRunning,
                onRunClick = {
                    coroutineScope.launch {
                        isRunning = true
                        outputState = OutputPaneUiState.Running
                        val result = withContext(Dispatchers.IO) {
                            runScript(editorValue.text)
                        }
                        outputState = when (result) {
                            is ScriptExecutionResult.Success ->
                                OutputPaneUiState.Success(result.output, result.exitCode)
                            is ScriptExecutionResult.Error ->
                                OutputPaneUiState.Error(result.errorMessage, result.exitCode)
                        }
                        isRunning = false
                    }
                },
                onResetClick = {
                    editorValue = TextFieldValue("")
                    outputState = OutputPaneUiState.Idle
                    cursorPosition = null
                }
            )
        }
    }
}

@Composable
fun ButtonRow(
    isEnabled: Boolean,
    onRunClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Run button: green neon background with a larger play icon.
        Button(
            onClick = onRunClick,
            enabled = !isEnabled,
        ) {
            if (!isEnabled) {
                Text(text = stringResource(Res.string.btn_run_script))

            } else {
                Text(text = stringResource(Res.string.btn_running))
            }
        }
        // Reset button.
        Button(
            onClick = onResetClick,
            enabled = !isEnabled
        ) {
            Text(text = stringResource(Res.string.btn_reset))
        }
    }
}
