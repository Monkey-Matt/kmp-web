### Learning to run Kotlin Multiplatform on the web. Hosted on Github Pages, [see here.](https://monkey-matt.github.io/KMP-Web-Executable/)

This project is targeting Web, Android, and iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

  
Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/),
[KMP Setup Wizard](https://kmp.jetbrains.com/)...

Tutorials used
[Kotlin web setup guide](https://kotlinlang.org/docs/wasm-get-started.html#before-you-start),
[Collapsing toolbar](https://www.droidcon.com/2022/10/10/collapsing-toolbar-with-parallax-effect-and-curved-motion-in-jetpack-compose-%F0%9F%98%8E/)

### Running locally

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

### Building releases

A release version is built using `:composeApp:wasmJsBrowserDistribution` Gradle task. The output files (found in `composeApp/build/dist/wasmJs/productionExecutable`) are saved to [a separate repo](https://github.com/Monkey-Matt/KMP-Web-Executable) where Github Pages is enabled, automatically deploying any changes from the `main` branch.
