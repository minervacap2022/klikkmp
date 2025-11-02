# Klik Calendar KMP iOS

This workspace hosts the Kotlin Multiplatform implementation of the Klik iOS client defined in `Designs.md`. The shared Compose UI is delivered from the `shared` module and embedded in the native target through the generated `shared.xcframework`.

## Prerequisites
- macOS with Xcode 15+
- JDK 17 or later (`/usr/libexec/java_home -v 17` must resolve)
- Kotlin Multiplatform toolchain (installed automatically via Gradle)

Export `JAVA_HOME` if it is not already configured:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

## One-time setup
```sh
cd "$(dirname "$0")"
./gradlew :shared:assembleSharedDebugXCFramework
```

This creates `shared/build/XCFrameworks/Debug/shared.xcframework` that Xcode consumes.

## Xcode build
1. Open `Klik_calendar_new.xcodeproj` in Xcode.
2. Select the `Klik_calendar_new` target.
3. Build or run. The `Kotlin XCFramework` run script invokes Gradle per configuration (`Debug`/`Release`) and copies the fresh framework into the bundle before linking.

## Updating shared logic or UI
- Modify Compose screens in `shared/src/commonMain/kotlin/com/klikcalendar/app/ui`.
- State/data models live under `shared/src/commonMain/kotlin/com/klikcalendar/app/state` and `.../model`.
- Regenerate XCFrameworks via `./gradlew :shared:assembleSharedDebugXCFramework` (or `Release`) when dependencies change.

## Directory layout
- `shared/` — Kotlin Multiplatform module with Compose Multiplatform UI, state, and sample data.
- `Klik_calendar_new/` — Native iOS host target (Objective‑C) embedding the Compose view controller.
- `Klik_calendar_new.xcodeproj/` — Xcode project configured to run the Gradle build phase and link the generated framework.

## Troubleshooting
- If Xcode reports it cannot find the `shared` framework, ensure the Gradle build succeeded and the XCFramework exists under `shared/build/XCFrameworks/<Config>/`.
- For Java errors, confirm `JAVA_HOME` points to a valid JDK 17+.
- Clear cached frameworks by deleting `shared/build` and rebuilding via Gradle.
