package org.rbbozkurt.composescriptrunner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.rbbozkurt.composescriptrunner.script.ScriptExecutionResult
import org.rbbozkurt.composescriptrunner.script.runScript
import org.rbbozkurt.composescriptrunner.ui.components.EditorPane
import org.rbbozkurt.composescriptrunner.ui.components.OutputPane
import org.rbbozkurt.composescriptrunner.ui.state.OutputUiState

@Composable
fun MainScreen() {
    var scriptText by remember { mutableStateOf("") }
    var outputState by remember { mutableStateOf<OutputUiState>(OutputUiState.Idle) }
    var cursorPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var runRequested by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(false) }

    // Fullscreen container with background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) // IntelliJ-like dark gray
            .padding(16.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.weight(1f)) {
                Box(Modifier.weight(1f).fillMaxHeight()) {
                    EditorPane(
                        scriptText = scriptText,
                        onTextChange = { scriptText = it },
                        cursorPosition = cursorPosition
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(Modifier.weight(1f).fillMaxHeight()) {
                    OutputPane(
                        state = outputState,
                        onNavigateToError = { line, column ->
                            cursorPosition = line to column
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { runRequested = true },
                enabled = !isRunning,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (isRunning) "Running..." else "Run Script")
            }
        }
    }

    // Run script on demand
    LaunchedEffect(runRequested) {
        if (runRequested) {
            isRunning = true
            outputState = OutputUiState.Running

            val result = withContext(Dispatchers.IO) {
                runScript(scriptText)
            }

            outputState = when (result) {
                is ScriptExecutionResult.Success -> OutputUiState.Success(result.output, result.exitCode)
                is ScriptExecutionResult.Error -> OutputUiState.Error(result.errorMessage, result.exitCode)
            }

            isRunning = false
            runRequested = false
        }
    }
}
