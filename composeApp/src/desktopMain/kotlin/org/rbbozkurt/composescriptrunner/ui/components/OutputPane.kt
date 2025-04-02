// File: EditorPane.kt
package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rbbozkurt.composescriptrunner.ui.state.OutputUiState


@Composable
fun OutputPane(
    state: OutputUiState,
    modifier: Modifier = Modifier,
    onNavigateToError: ((Int, Int) -> Unit)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when (state) {
            is OutputUiState.Idle -> {
                Text("\uD83D\uDCA4 Waiting to run script...", color = Color.Gray)
            }
            is OutputUiState.Running -> {
                Text("\u23F3 Script is running...", color = Color.Yellow)
            }
            is OutputUiState.Success -> {
                Text("✅ Exit Code: ${state.exitCode}", color = Color.Green)
                Spacer(Modifier.height(8.dp))
                Text(state.output, color = Color.Green, fontFamily = FontFamily.Monospace)
            }
            is OutputUiState.Error -> {
                Text("❌ Error occurred", color = Color.Red)
                state.exitCode?.let {
                    Text("Exit Code: $it", color = Color.Red)
                }
                Spacer(Modifier.height(8.dp))

                val regex = Regex(""".*\.kts:(\d+):(\d+): error: (.*)""")
                val match = regex.find(state.message)
                if (match != null) {

                    val (line, column, msg) = match.destructured
                    Text(
                        text = "script:${line}:${column}: error:$msg",
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace,
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        modifier = Modifier
                            .background(Color(0x1AFF5252), shape = RoundedCornerShape(4.dp)) // subtle rounded background
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                            .clickable {
                                onNavigateToError(line.toInt(), column.toInt())
                            }
                    )
                } else {
                    Text(state.message, color = Color.Red, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
