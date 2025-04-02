package org.rbbozkurt.composescriptrunner.ui.state

sealed class OutputPaneUiState {
    object Idle : OutputPaneUiState()

    object Running : OutputPaneUiState()

    data class Success(
        val output: String,
        val exitCode: Int
    ) : OutputPaneUiState()

    data class Error(
        val message: String,
        val exitCode: Int? = null
    ) : OutputPaneUiState()
}
