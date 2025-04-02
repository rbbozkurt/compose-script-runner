# 🚀 ComposeScriptRunner

**ComposeScriptRunner** is a GUI tool built with [Jetpack Compose](https://developer.android.com/jetpack/compose) that allows users to write, execute, and debug Kotlin scripts—all within a single integrated interface. With live output, syntax highlighting, and clickable error messages, it offers a seamless scripting experience similar to modern IDEs like IntelliJ IDEA.

---

## 🎯 Features

- **Side-by-Side Panes:**  
  An **editor pane** for script input and an **output pane** for live execution results.

- **Live Script Execution:**  
  Executes scripts using `kotlinc -script` and displays real-time output and error messages.

- **Syntax Highlighting:**  
  Highlights a predefined set of Kotlin keywords in vivid colors for better readability.

- **Clickable Error Messages:**  
  Error locations (e.g., `script:2:1: error: ...`) are clickable, navigating the editor to the exact cursor position.

- **Execution Status Indicators:**  
  Visual cues indicate when a script is running and whether it exited with an error.

---

## 📂 Project Structure

```plaintext
compose-script-runner/
├── desktopMain/
│   ├── kotlin/
│      └── org/rbbozkurt/composescriptrunner/
│          ├── script/             # Script execution logic
│          └── ui/                 # UI components and state
│          │    └── MainScreen.kt  # Main UI for the app
│          ├── App.kt
│          └── main.kt             # entry point
├── build.gradle.kts               # Gradle build configuration
└── README.md                      # Project documentation
```

## Prerequisites

- **Java 17+** : Ensure you have JDK 17 (or later) installed.
- **Gradle** : The project uses the Gradle wrapper (`./gradlew`), so no local Gradle installation is required.
- **Kotlin Multiplatform & Compose** : All necessary dependencies are automatically resolved during the build process.
- **Kotlin Compiler (kotlinc) ⚠️** : To execute scripts, ComposeScriptRunner calls `kotlinc -script`.
Please install the Kotlin compiler and ensure it's accessible via your PATH (or via `/usr/bin/env kotlinc`).

## 🏃‍♂️ How to Build & Run

1. **Clone the Repository:**
```bash
git clone https://github.com/rbbozkurt/compose-script-runner.git
cd compose-script-runner
```
2. **Build the Project:**
```bash
./gradlew build
```

3. **Run the Application:**
```bash
./gradlew run
```
## 🎬 Usage

1. **Write Your Script:**
   - Enter your Kotlin script in the editor pane.

2. **Execute:**
   - Click the green Run button (with a play icon) to execute your script.
   - Live output and errors will appear in the output pane.

3. **Navigate to Errors:**
   - Click on error messages in the output pane to jump directly to the problematic line in the editor.

4. **Reset:**
   - Use the Reset button to clear both the editor and output pane for a fresh start.


## 📸 Demo


## 💡 Extensibility

Future improvements may include:

- **Support for Additional Languages:**
  - Extend the tool to support Swift or other scripting languages.

- **Enhanced Syntax Highlighting:**
  - Add more keywords or advanced language parsing.

- **Advanced Error Reporting:**
  - Provide richer error diagnostics and log information.

## 📧 Contact

👤 **R. Berkay Bozkurt**  
📧 Email: resitberkaybozkurt@gmail.com  
📂 GitHub: [github.com/rbbozkurt](https://github.com/rbbozkurt)