// File: EditorPane.kt
package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

private val keywordColors = mapOf(
    "fun" to Color(0xFFCC7832),     // Orange
    "val" to Color(0xFF9876AA),     // Purple
    "var" to Color(0xFF9876AA),     // Purple
    "if" to Color(0xFFCC7832),      // Orange
    "else" to Color(0xFFCC7832),    // Orange
    "for" to Color(0xFFCC7832),     // Orange
    "while" to Color(0xFFCC7832),   // Orange
    "return" to Color(0xFFCC7832),  // Orange
    "class" to Color(0xFF0000FF),   // Blue
    "object" to Color(0xFF0000FF)   // Blue
)

@Composable
fun EditorPane(
    scriptText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    cursorPosition: Pair<Int, Int>? = null
) {
    var fieldValue by remember { mutableStateOf(TextFieldValue(scriptText)) }

    fun moveCursorTo(line: Int, column: Int) {
        val lines = fieldValue.text.lines()
        val offset = lines.take(line - 1).sumOf { it.length + 1 } + (column - 1).coerceAtLeast(0)
        fieldValue = fieldValue.copy(selection = TextRange(offset))
    }

    LaunchedEffect(cursorPosition) {
        cursorPosition?.let { (line, column) ->
            moveCursorTo(line, column)
        }
    }

    BasicTextField(
        value = fieldValue,
        onValueChange = {
            fieldValue = it
            onTextChange(it.text)
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
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

private fun highlightSyntax(text: String): AnnotatedString {
    val tokens = text.split(Regex("(?=\\W)|(?<=\\W)"))
    return buildAnnotatedString {
        tokens.forEach { token ->
            val color = keywordColors[token] ?: Color.White
                withStyle(style = SpanStyle(color = color)) {
                    append(token)
            }
        }
    }
}