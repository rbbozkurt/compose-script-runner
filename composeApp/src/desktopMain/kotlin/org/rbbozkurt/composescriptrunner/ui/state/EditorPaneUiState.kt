package org.rbbozkurt.composescriptrunner.ui.state

import androidx.compose.ui.text.input.TextFieldValue

sealed class EditorPaneUiState(
    open val textFieldValue: TextFieldValue
) {
    data class Editing(override val textFieldValue: TextFieldValue) : EditorPaneUiState(textFieldValue)
    data class ReadOnly(override val textFieldValue: TextFieldValue) : EditorPaneUiState(textFieldValue)
}
