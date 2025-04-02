package org.rbbozkurt.composescriptrunner.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun EditorPane(
    scriptText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = scriptText,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxSize(),
        textStyle = TextStyle(color = Color.White)
    )
}

