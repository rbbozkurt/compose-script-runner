package org.rbbozkurt.composescriptrunner.script

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

sealed class ScriptExecutionResult {
    data class Success(val output: String, val exitCode: Int) : ScriptExecutionResult()
    data class Error(val errorMessage: String, val exitCode: Int) : ScriptExecutionResult()
}

suspend fun runScript(script: String): ScriptExecutionResult {
    return withContext(Dispatchers.IO) {
        val tempFile = File.createTempFile("temp_script", ".kts")
        tempFile.writeText(script)

        val process = ProcessBuilder("/usr/bin/env", "kotlinc", "-script", tempFile.absolutePath)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            ScriptExecutionResult.Success(output, exitCode)
        } else {
            ScriptExecutionResult.Error(output, exitCode)
        }
    }
}
