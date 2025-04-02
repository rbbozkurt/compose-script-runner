package org.rbbozkurt.composescriptrunner

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rbbozkurt.composescriptrunner.ui.MainScreen

/**
 * The entry point for the Compose Script Runner app preview.
 *
 * This composable function wraps [MainScreen] within a [MaterialTheme] to apply
 * Material design styling. It is annotated with [Preview] so that it can be rendered
 * in the IDE for design and testing purposes.
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen()
    }
}
