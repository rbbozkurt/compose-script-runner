package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.TextUnit
import composescriptrunner.composeapp.generated.resources.*
import composescriptrunner.composeapp.generated.resources.Res
import composescriptrunner.composeapp.generated.resources.exit_code
import composescriptrunner.composeapp.generated.resources.script_running
import composescriptrunner.composeapp.generated.resources.waiting_message
import org.jetbrains.compose.resources.stringResource

import org.rbbozkurt.composescriptrunner.ui.state.OutputPaneUiState

/**
 * Displays a waiting message when no script is running.
 *
 * @param modifier A [Modifier] for this composable.
 * @param commonFontSize The font size to be used for the text.
 */
@Composable
fun IdleOutput(
    modifier: Modifier = Modifier,
    commonFontSize: TextUnit
) {
    Text(
        text = stringResource(Res.string.waiting_message),
        color = Color(0xFFCCCCCC),
        style = TextStyle(fontSize = commonFontSize),
        modifier = modifier
    )
}

/**
 * Displays a message indicating that the script is running.
 *
 * @param modifier A [Modifier] for this composable.
 * @param commonFontSize The font size to be used for the text.
 */
@Composable
fun RunningOutput(
    modifier: Modifier = Modifier,
    commonFontSize: TextUnit
) {
    Text(
        text = stringResource(Res.string.script_running),
        color = Color(0xFFFFE082), // soft yellow
        style = TextStyle(fontSize = commonFontSize),
        modifier = modifier
    )
}

/**
 * Displays the successful script output.
 *
 * This composable shows the exit code and the output text.
 *
 * @param exitCode The exit code returned from the script execution.
 * @param output The output text of the successful script run.
 * @param modifier A [Modifier] for this composable.
 * @param commonFontSize The font size to be used for the text.
 */
@Composable
fun SuccessOutput(
    exitCode: Int,
    output: String,
    modifier: Modifier = Modifier,
    commonFontSize: TextUnit
) {
    Column(modifier = modifier) {
        Text(
            stringResource(Res.string.exit_code, exitCode),
            color = Color(0xFF8BC34A), // soft green
            style = TextStyle(fontSize = commonFontSize)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            output,
            color = Color(0xFF8BC34A),
            fontFamily = FontFamily.Monospace,
            style = TextStyle(fontSize = commonFontSize)
        )
    }
}

/**
 * Displays error output from script execution.
 *
 * If the error message contains a specific error format (extracted via regex),
 * it shows a clickable error message that can navigate to the error location.
 *
 * @param message The full error message produced by the script execution.
 * @param exitCode The exit code returned from the failed script execution.
 * @param modifier A [Modifier] for this composable.
 * @param commonFontSize The font size to be used for the text.
 * @param onNavigateToError Callback invoked with line and column when the error message is clicked.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ErrorOutput(
    message: String,
    exitCode: Int?,
    modifier: Modifier = Modifier,
    commonFontSize: TextUnit,
    onNavigateToError: (Int, Int) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.error_occurred),
            color = Color(0xFFE57373), // soft red
            style = TextStyle(fontSize = commonFontSize)
        )
        exitCode?.let {
            Text(
                stringResource(Res.string.exit_code, it),
                color = Color(0xFFE57373),
                style = TextStyle(fontSize = commonFontSize)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Regex to parse error details from the message.
        val regex = Regex(""".*\.kts:(\d+):(\d+): error: (.*)""")
        val match = regex.find(message)
        if (match != null) {
            val (line, column, msg) = match.destructured
            var isHovered by remember { mutableStateOf(false) }
            // Change background color on hover.
            val backgroundColor = if (isHovered) Color(0x33FFFFFF) else Color(0x1AFF5252)
            Text(
                text = "script:$line:$column: error: $msg",
                color = Color(0xFF80CBC4).copy(alpha = 0.8f),
                fontFamily = FontFamily.Monospace,
                style = TextStyle(
                    fontSize = commonFontSize,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .onPointerEvent(PointerEventType.Enter, onEvent = { event: PointerEvent ->
                        isHovered = true
                    })
                    .onPointerEvent(PointerEventType.Exit, onEvent = { event: PointerEvent ->
                        isHovered = false
                    })
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable {
                        onNavigateToError(line.toInt(), column.toInt())
                    }
            )
        } else {
            Text(
                message,
                color = Color(0xFFE57373),
                fontFamily = FontFamily.Monospace,
                style = TextStyle(fontSize = commonFontSize)
            )
        }
    }
}

/**
 * Displays the output pane containing the script execution output.
 *
 * This composable chooses between different output views based on the [OutputPaneUiState]
 * (Idle, Running, Success, or Error) and displays the appropriate content.
 *
 * @param state The current state of the output pane.
 * @param modifier A [Modifier] for this composable.
 * @param onNavigateToError Callback invoked with line and column when an error message is clicked.
 */
@Composable
fun OutputPane(
    state: OutputPaneUiState,
    modifier: Modifier = Modifier,
    onNavigateToError: (Int, Int) -> Unit
) {
    // Define a common font size for consistency.
    val commonFontSize = 14.sp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when (state) {
            is OutputPaneUiState.Idle -> IdleOutput(commonFontSize = commonFontSize)
            is OutputPaneUiState.Running -> RunningOutput(commonFontSize = commonFontSize)
            is OutputPaneUiState.Success -> SuccessOutput(
                exitCode = state.exitCode,
                output = state.output,
                commonFontSize = commonFontSize
            )
            is OutputPaneUiState.Error -> ErrorOutput(
                message = state.message,
                exitCode = state.exitCode,
                commonFontSize = commonFontSize,
                onNavigateToError = onNavigateToError
            )
        }
    }
}