# PaymentAuthSDK

A Kotlin Multiplatform (KMP) SDK for Android that provides secure payment authentication using biometric (fingerprint/face) verification built with Jetpack Compose.

---

## Quick Navigation

- [Requirements](#requirements)
- [Building the AAR](#building-the-aar)
- [Integration Guide — Android Project](#integration-guide--android-project)
- [Integration Guide — KMP Project](#integration-guide--kmp-project)
- [Usage](#usage)
- [Transitive Dependencies](#transitive-dependencies)
- [Troubleshooting](#troubleshooting)

---

## Requirements

| Requirement | Version |
|---|---|
| Android minSdk | 26 (Android 8.0) |
| Android compileSdk | 36 |
| Kotlin | 2.0+ |
| Jetpack Compose BOM | 2024+ |

---

## Building the AAR

Follow these steps to build the `payment_sdk.aar` from the SDK source project.

### Prerequisites

- Android Studio (Hedgehog or later recommended)
- JDK 11
- Gradle 8+

---

### Step 1 — Clone the repository

```bash
git clone https://github.com/your-username/PaymentAuthSDK.git
cd PaymentAuthSDK
```

---

### Step 2 — Open in Android Studio

Open the project in Android Studio and wait for the initial Gradle sync to complete.

---

### Step 3 — Build the AAR via Terminal

Run the following command from the **project root**:

```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Release build (recommended for distribution)
./gradlew :composeApp:assembleRelease
```

> Replace `:composeApp` with your actual module name if different. Run `./gradlew projects` to confirm your module name.

---

### Step 4 — Locate the output AAR

After a successful build, the AAR will be generated at:

```
# Debug
composeApp/build/outputs/aar/composeApp-debug.aar

# Release
composeApp/build/outputs/aar/composeApp-release.aar
```

Rename it before distributing:

```bash
cp composeApp/build/outputs/aar/composeApp-release.aar /your/destination/payment_sdk.aar
```

---

### Step 5 — Build via Android Studio UI (alternative)

If you prefer using the IDE instead of the terminal:

1. Open the **Gradle panel** on the right side of Android Studio
2. Navigate to **`composeApp → Tasks → build`**
3. Double-click **`assembleRelease`**
4. Find the output at `composeApp/build/outputs/aar/`

---

### Build Variants

| Command | Output | Use for |
|---|---|---|
| `assembleDebug` | `composeApp-debug.aar` | Development & testing |
| `assembleRelease` | `composeApp-release.aar` | Production distribution |

> **Note:** The release build has `isMinifyEnabled = false` by default. If you enable minification, you must also ship a `consumer-rules.pro` ProGuard file alongside the AAR so consuming apps can apply the rules correctly.

---

## Integration Guide — Android Project

### Step 1 — Download the AAR

Download the latest `payment_sdk.aar` from the [Releases](../../releases) page.

---

### Step 2 — Add the AAR to your project

Place the downloaded `payment_sdk.aar` inside your app module's `libs` folder:

```
your-project/
  app/
    libs/
      payment_sdk.aar   ← place it here
    src/
    build.gradle.kts
```

Create the `libs` folder if it doesn't exist.

---

### Step 3 — Update `settings.gradle.kts`

In your **root** `settings.gradle.kts`, add `app/libs` to the `flatDir` repository:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        flatDir {
            dirs("app/libs")
        }
    }
}
```

---

### Step 4 — Update `app/build.gradle.kts`

Add the AAR and all required transitive dependencies:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.your.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.your.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // PaymentAuthSDK
    implementation(files("libs/payment_sdk.aar"))

    // Required transitive dependencies for PaymentAuthSDK
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
}
```

> **Why transitive dependencies?** AAR files do not bundle their own dependencies. Any library used internally by the SDK must also be declared in your app. See the [Transitive Dependencies](#transitive-dependencies) section for details.

---

### Step 5 — Update `AndroidManifest.xml`

The SDK requires biometric and internet permissions. Add these to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
<uses-permission android:name="android.permission.INTERNET" />
```

---

### Step 6 — Use `FragmentActivity`

The SDK uses the Biometric API which requires `FragmentActivity`. Make sure your `MainActivity` extends `FragmentActivity` (not `ComponentActivity`):

```kotlin
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    ...
}
```

---

## Integration Guide — KMP Project

> The SDK is Android-only. In a KMP project, the AAR must be added to the **`androidMain`** source set only — never `commonMain`.

### Step 1 — Download the AAR

Download the latest `payment_sdk.aar` from the [Releases](../../releases) page.

---

### Step 2 — Add the AAR to your KMP module

Place the AAR inside your shared KMP module's `libs` folder (typically `composeApp` or `shared`):

```
your-kmp-project/
  composeApp/
    libs/
      payment_sdk.aar   ← place it here
    src/
    build.gradle.kts
  iosApp/
  build.gradle.kts
  settings.gradle.kts
```

---

### Step 3 — Update `settings.gradle.kts`

In your **root** `settings.gradle.kts`, point `flatDir` to the KMP module's `libs` folder:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        flatDir {
            dirs("composeApp/libs")  // ← match your KMP module name
        }
    }
}
```

---

### Step 4 — Update `composeApp/build.gradle.kts`

Add the AAR under `androidMain.dependencies` so it is only included for the Android target:

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary) // or androidApplication if it's an app
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.fragment.ktx)

            // PaymentAuthSDK — Android only
            implementation(files("libs/payment_sdk.aar"))

            // Biometric is Android-only, must stay in androidMain
            implementation("androidx.biometric:biometric:1.2.0-alpha05")
        }
    }
}

android {
    namespace = "com.your.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
```

---

### Step 5 — Update `AndroidManifest.xml`

In your KMP module, add permissions to `composeApp/src/androidMain/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
<uses-permission android:name="android.permission.INTERNET" />
```

---

### Step 6 — Use `FragmentActivity` in the Android entry point

In `composeApp/src/androidMain/`, ensure your `MainActivity` extends `FragmentActivity`:

```kotlin
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    ...
}
```

---

### Step 7 — Call the SDK from `androidMain` only

Since the SDK is Android-only, call `LaunchPaymentSDK` from an Android-specific file inside `androidMain`, **not** from `commonMain`:

```kotlin
// composeApp/src/androidMain/kotlin/com/your/app/MainActivity.kt

package com.your.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.LaunchPaymentSDK

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchPaymentSDK()
        }
    }
}
```

> ⚠️ **Do not import `LaunchPaymentSDK` from `commonMain`** — it is an Android-only class and will cause a compilation error on iOS targets.

---

## Usage

### Basic Integration

```kotlin
package com.your.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.bijoy.paymentauth.LaunchPaymentSDK

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchPaymentSDK()
        }
    }
}
```

### Launch from a Button

```kotlin
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bijoy.paymentauth.LaunchPaymentSDK

@Composable
fun HomeScreen() {
    var showPayment by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showPayment = true }) {
            Text("Pay Now")
        }

        if (showPayment) {
            LaunchPaymentSDK()
        }
    }
}
```

---

## Transitive Dependencies

The SDK internally uses the following libraries. These must be declared in your app's `build.gradle.kts`:

| Library | Version | Purpose | Scope |
|---|---|---|---|
| `androidx.biometric:biometric` | `1.2.0-alpha05` | Fingerprint / face authentication | `androidMain` only |
| `androidx.navigation:navigation-compose` | `2.7.7` | In-SDK screen navigation | `commonMain` |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | `1.7.0` | JSON serialization | `commonMain` |
| `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.8.0` | ViewModel support | `commonMain` |
| `androidx.lifecycle:lifecycle-runtime-compose` | `2.8.0` | Lifecycle-aware Compose state | `commonMain` |

> In a KMP project, `biometric` must go in `androidMain.dependencies` since it is Android-only. The rest can live in `commonMain.dependencies` as they are Multiplatform-compatible.

---

## Troubleshooting

### `Unresolved reference 'LaunchPaymentSDK'`
- Confirm `payment_sdk.aar` is at the correct `libs/` path
- Confirm `settings.gradle.kts` has the correct `flatDir` path
- Do **File → Invalidate Caches & Restart** in Android Studio
- Re-sync Gradle

### `Unresolved reference` in KMP `commonMain`
- `LaunchPaymentSDK` is Android-only — move the import to `androidMain`, never `commonMain`

### `FragmentActivity` crash / biometric not working
- Ensure `MainActivity` extends `FragmentActivity`, not `ComponentActivity`

### Biometric permission denied
- Confirm `USE_BIOMETRIC` permission is in `AndroidManifest.xml`
- Ensure the device has biometric hardware enrolled

---

## License

```
Copyright (c) 2025 Bijoy
All rights reserved.
```
