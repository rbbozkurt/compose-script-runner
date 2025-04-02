package org.rbbozkurt.composescriptrunner

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * The main entry point for the ComposeScriptRunner desktop application.
 *
 * This function launches the Compose application using the [application] builder.
 * It creates a window titled "ComposeScriptRunner" that will call [exitApplication]
 * when the window is closed. The window's content is provided by the [App] composable.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeScriptRunner",
    ) {
        App()
    }
}
