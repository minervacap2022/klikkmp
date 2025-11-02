# Build Instructions

## Requirements

The Xcode build requires **Java 17** to compile the Kotlin Multiplatform shared framework.

## Setup

Java 17 is already installed on your system at:
```
/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

The Xcode build script ("Kotlin XCFramework" phase) will automatically detect and use this Java installation.

## Building from Xcode

Simply build the project in Xcode (⌘B). The build script will:

1. Automatically detect Java 17
2. Clean any cached builds
3. Compile the Kotlin shared code for all iOS architectures (arm64, x86_64, simulator)
4. Create the XCFramework
5. Copy it to the appropriate location for Xcode

## If the Build Fails

If you see "Unable to locate a Java Runtime", verify Java 17 is installed:

```bash
ls -la /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

If the directory doesn't exist, install Java 17:

```bash
brew install openjdk@17
```

## Manual Build (Optional)

You can also build the shared framework manually from the terminal:

```bash
cd "/Users/minervatech/Library/Mobile Documents/com~apple~CloudDocs/Minerva Capital/项目-孵化/202509 小浣熊计划/13. Misc/VOX_glue/kilkcalendar2/Klikcalendar3/Klik_calendar_new"

export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew :shared:assembleSharedDebugXCFramework
```

The output will be at:
```
shared/build/XCFrameworks/debug/shared.xcframework
```

## Current Build Status

✅ Build compiles successfully
✅ All placeholder data removed (production-ready)
✅ Production-ready architecture implemented
✅ Java 17 installed and configured
✅ XCFramework builds successfully for all architectures (arm64, x86_64, simulator)
✅ Kotlin Multiplatform compatibility issues resolved
✅ Backend API integration ready (using Ktor client 2.3.12)

## Recent Fixes Applied

- Fixed KMP compatibility issues with `System.currentTimeMillis()` → `Clock.System.now().toEpochMilliseconds()`
- Fixed KMP compatibility issues with `String.format()` → manual string padding with `padStart()`
- Fixed iOS audio format constant `kAudioFormatMPEG4AAC` → literal value 1633772320u
- Fixed missing kotlinx.datetime imports in AppState.kt
- Added Ktor HTTP client dependencies (stable version 2.3.12)
- Added kotlinx-serialization support

## Warnings (Safe to Ignore)

The build shows some deprecation warnings:
- Xcode 26.0 compatibility warning (functionality works fine)
- Deprecated icon references (cosmetic only, doesn't affect functionality)
- Deprecated memory model switch (handled by Kotlin compiler)
- Deprecated LinearProgressIndicator API (still functional)

These warnings don't affect the app's functionality and can be addressed later.

## Build Output Location

After a successful build, the XCFramework will be located at:
```
shared/build/XCFrameworks/debug/shared.xcframework
```

The framework includes:
- ios-arm64 (physical devices)
- ios-arm64_x86_64-simulator (simulator on both Intel and Apple Silicon Macs)
