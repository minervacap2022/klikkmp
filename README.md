# Klik Calendar - Android App

> **[简体中文版 README](README.zh-CN.md)** | English

A modern Android calendar and workflow management application built with Kotlin Multiplatform and Jetpack Compose.

## 📱 Overview

Klik is a comprehensive work management Android application that integrates calendar, meeting transcription, task management, and knowledge graph features. Built with Kotlin Multiplatform (KMP) technology stack and Jetpack Compose UI. **Currently focused on Android platform**.

## ✨ Key Features

- 📅 **Calendar Management** - Timeline view with event tracking
- 🎙️ **Hardware Integration** - Battery & Bluetooth status monitoring (placeholder data)
- 📝 **Feedback System** - Swipeable card interface with 10 feedback items
- 🌐 **Knowledge Graph** - Visualize relationships between people, teams, projects
- ✅ **Task Management** - Operations tracking with automation
- 🌍 **Multi-language** - Chinese (default) and English support

## 🏗️ Tech Stack

- **Platform**: Android (developed with Android Studio)
- **UI Framework**: Jetpack Compose Multiplatform
- **Language**: Kotlin
- **Architecture**: MVVM + Repository Pattern
- **Concurrency**: Kotlin Coroutines + Flow
- **Date/Time**: kotlinx-datetime
- **Build Tool**: Gradle (Kotlin DSL)

## 🚀 Quick Start

### Requirements
- **JDK 17** or higher
- **Android Studio Hedgehog (2023.1.1)** or newer
- **Gradle 8.0+** (included via Gradle Wrapper)
- **Android SDK** (install via Android Studio)
- Recommended: Android emulator or physical device

### Build Instructions

#### Method 1: Using Android Studio (Recommended)

1. **Clone the repository**
```bash
git clone https://github.com/minervacap2022/klikkmp.git
```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open" to open the project
   - Choose the cloned `klikkmp` folder
   - Wait for Gradle sync to complete

3. **Run the app**
   - Click the "Run" button (green triangle) in the toolbar
   - Or press `Shift + F10` (Windows/Linux) or `Control + R` (Mac)
   - Select an Android emulator or connected device

#### Method 2: Command Line

1. **Clone and navigate**
```bash
git clone https://github.com/minervacap2022/klikkmp.git
cd klikkmp
```

2. **Build project**
```bash
./gradlew build
```

3. **Install on device**
```bash
# Debug build
./gradlew :app:installDebug

# Release build
./gradlew :app:installRelease
```

## ⚠️ Placeholder Data

**Current version uses placeholder data for UI development and testing.**

The following features use placeholder data (marked with `⚠️ PLACEHOLDER_REMOVE` in code):
- ✅ Battery status (shows 85%)
- ✅ Bluetooth status (shows "Online")
- ✅ Feedback cards (10 test cards)
- ✅ Calendar events
- ✅ Meeting transcripts
- ✅ Knowledge graph nodes

### Removing Placeholder Data

When ready to integrate with real backend, search for the marker:

```bash
grep -r "PLACEHOLDER_REMOVE" .
```

## 📂 Project Structure

```
Klik_UI/
├── app/                    # Android app module
├── shared/                 # Shared code module (KMP)
│   └── src/main/kotlin/
│       └── com/klikcalendar/app/
│           ├── data/       # Data layer (Repository)
│           ├── model/      # Data models
│           ├── state/      # App state management
│           ├── strings/    # Localization strings
│           └── ui/         # UI components & screens
└── gradle/                 # Gradle configuration
```

## 🔧 Hardware Integration

The app is prepared for hardware integration:

### Battery Status (Android)
- Platform API: `BatteryManager`
- Use `BroadcastReceiver` to listen for `ACTION_BATTERY_CHANGED`
- Polling frequency: 30-60 seconds
- Status levels:
  - Normal: Battery > 20%
  - Warning: Battery 10-20%
  - Critical: Battery < 10%

### Bluetooth (Android)
- Platform API: `BluetoothAdapter`
- Real-time connection status monitoring
- Supports BLE (Bluetooth Low Energy)
- Requires `BLUETOOTH` and `BLUETOOTH_ADMIN` permissions

See documentation in `StatusOverlay.kt` for detailed integration guide.

## 📝 TODO

- [ ] Integrate with real backend API
- [ ] Implement user authentication
- [ ] Add dark mode support
- [ ] Add unit tests and UI tests
- [ ] Implement offline data caching
- [ ] Add Android push notifications
- [ ] Implement hardware integration (battery, bluetooth)
- [ ] Optimize Android performance
- [ ] Future: iOS platform support (KMP ready)

## 📄 License

MIT License. See LICENSE file for details.

## 📧 Contact

For questions or suggestions, please use GitHub Issues.

---

**Note**: This project is under active development. Some features may use placeholder data.
