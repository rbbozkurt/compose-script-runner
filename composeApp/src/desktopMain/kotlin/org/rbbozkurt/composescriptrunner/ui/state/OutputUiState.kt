package org.rbbozkurt.composescriptrunner.ui.state

sealed class OutputUiState {
    object Idle : OutputUiState()

    object Running : OutputUiState()

    data class Success(
        val output: String,
        val exitCode: Int
    ) : OutputUiState()

    data class Error(
        val message: String,
        val exitCode: Int? = null
    ) : OutputUiState()
}
