package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.rbbozkurt.composescriptrunner.ui.state.OutputUiState

@Composable
fun OutputPane(
    state: OutputUiState,
    modifier: Modifier = Modifier
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
                Text("💤 Waiting to run script...", color = Color.Gray)
            }
            is OutputUiState.Running -> {
                Text("⏳ Script is running...", color = Color.Yellow)
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
                Text(state.message, color = Color.Red, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

