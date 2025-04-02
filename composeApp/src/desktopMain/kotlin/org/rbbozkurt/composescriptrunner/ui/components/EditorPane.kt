package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

/**
 * A mapping of Kotlin language keywords to specific colors used for syntax highlighting.
 */
private val keywordColors = mapOf(
    "fun" to Color(0xFFFF9800),     // Vivid Orange
    "val" to Color(0xFF9C27B0),     // Vivid Purple
    "var" to Color(0xFF9C27B0),     // Vivid Purple
    "if" to Color(0xFFFF9800),      // Vivid Orange
    "else" to Color(0xFFFF9800),    // Vivid Orange
    "for" to Color(0xFFFF9800),     // Vivid Orange
    "while" to Color(0xFFFF9800),   // Vivid Orange
    "return" to Color(0xFFFF9800),  // Vivid Orange
    "class" to Color(0xFF2196F3),   // Bright Blue
    "object" to Color(0xFF2196F3)   // Bright Blue
)

/**
 * A composable text editor that provides syntax highlighting.
 *
 * This editor uses a [BasicTextField] to allow users to edit text, applies a black background,
 * and displays syntax-highlighted text based on a set of known keywords.
 *
 * @param fieldValue The current [TextFieldValue] containing the text and selection information.
 * @param onValueChange Callback invoked when the text changes.
 * @param focusRequester A [FocusRequester] to control focus for the text field.
 * @param modifier Optional [Modifier] for styling and layout adjustments.
 */
@Composable
fun EditorPane(
    fieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = fieldValue,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester),
        textStyle = TextStyle(color = Color(0xFFEEEEEE), fontSize = 14.sp), // Near-white text
        cursorBrush = SolidColor(Color.White),
        decorationBox = { innerTextField ->
            innerTextField()
            val annotated = highlightSyntax(fieldValue.text)
            Text(
                text = annotated,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    )
}

/**
 * Generates an [AnnotatedString] with syntax highlighting applied.
 *
 * This function splits the given [text] into tokens based on non-word boundaries and applies
 * a [SpanStyle] with a specific color for tokens that match predefined keywords in [keywordColors].
 * If a token is not found in the map, a default near-white color is used.
 *
 * @param text The input text to highlight.
 * @return An [AnnotatedString] with the applied syntax highlighting.
 */
private fun highlightSyntax(text: String) =
    buildAnnotatedString {
        val tokens = text.split(Regex("(?=\\W)|(?<=\\W)"))
        tokens.forEach { token ->
            val color = keywordColors[token] ?: Color(0xFFEEEEEE)
            withStyle(style = SpanStyle(color = color)) {
                append(token)
            }
        }
    }
