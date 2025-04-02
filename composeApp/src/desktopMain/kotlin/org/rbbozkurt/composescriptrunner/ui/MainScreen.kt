package org.rbbozkurt.composescriptrunner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rbbozkurt.composescriptrunner.ui.components.EditorPane
import org.rbbozkurt.composescriptrunner.ui.components.OutputPane
import org.rbbozkurt.composescriptrunner.ui.state.OutputUiState

@Composable
fun MainScreen() {
    var scriptText by remember { mutableStateOf("") }
    var outputState by remember { mutableStateOf<OutputUiState>(OutputUiState.Idle) }
    var cursorPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Row(Modifier.fillMaxSize().padding(16.dp)) {
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
}
