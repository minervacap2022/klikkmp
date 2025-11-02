## Change Log (Automated)

- 2025-11-02: **PROJECT PUBLISHED TO GITHUB** - Initial public release as Android Studio project.
  - Repository: https://github.com/minervacap2022/klikkmp
  - Files created/updated:
    - `README.zh-CN.md`: Comprehensive README in simplified Chinese for Android development
    - `README.md`: English README clearly stating Android Studio project (replaced outdated iOS README)
    - `.gitignore`: Kotlin Multiplatform project gitignore configuration
  - Git initialized with 67 files and 250,233 lines of code
  - Repository metadata:
    - Description: "Klik Calendar - Android app built with Kotlin Multiplatform & Jetpack Compose. Features: calendar, feedback system, hardware integration (battery/bluetooth)"
    - Topics: android, android-studio, calendar-app, jetpack-compose, kotlin, kotlin-multiplatform
    - Language: Kotlin
  - Documentation emphasizes:
    - Android Studio as primary IDE
    - Android platform focus (not iOS)
    - Detailed Android build instructions
    - Android-specific hardware integration (BatteryManager, BluetoothAdapter)
  - All placeholder code clearly documented with `⚠️ PLACEHOLDER_REMOVE` markers

- 2025-11-02: Removed Recording floating action button from UI.
  - File: `shared/src/main/kotlin/com/klikcalendar/app/App.kt`
  - Change: Removed `floatingActionButton` parameter from Scaffold, which contained RecordingButton and RecordingStatusIndicator
  - Impact: Recording button and status indicator no longer displayed in the UI

- 2025-11-02: **CRITICAL FIX** - Connected SampleData to UI so battery/bluetooth indicators and feedback cards are now visible.
  - File: `shared/src/main/kotlin/com/klikcalendar/app/state/AppState.kt`
  - Change: Modified `rememberKlikAppState()` to use `SampleData` instead of empty data
  - Impact: Battery (85%), Bluetooth (Online), and other status indicators now visible in status bar. Feedback button appears and shows 10 feedback cards.
  - All SampleData usage clearly marked with `⚠️ PLACEHOLDER_REMOVE` comments for easy removal when integrating with backend

- 2025-11-02: Added placeholder data documentation and improved feedback button animation.
  - Files:
    - `shared/src/main/kotlin/com/klikcalendar/app/ui/components/StatusOverlay.kt`: Added prominent placeholder warning for battery/bluetooth data
    - `shared/src/main/kotlin/com/klikcalendar/app/ui/components/BottomActionBar.kt`: Changed feedback button animation from square to circular (scaleIn/scaleOut)
    - `shared/src/main/kotlin/com/klikcalendar/app/state/SampleData.kt`: Marked battery/bluetooth as placeholder data, expanded feedback items from 4 to 10
  - Change: All placeholder data now clearly marked with "⚠️ PLACEHOLDER_REMOVE" comments for easy removal
  - Impact: Feedback button now scales in/out smoothly in a circular manner. Added 6 more feedback samples for testing (10 total).
  - Removal Guide: Search codebase for "PLACEHOLDER_REMOVE" to find all placeholder code that should be removed when integrating with real hardware/backend

- 2025-11-02: Added Battery and confirmed Bluetooth (BLE) indicators to the status overlay UI.
  - Files:
    - `shared/src/main/kotlin/com/klikcalendar/app/model/DomainModels.kt`: Added `Battery` to `DeviceIndicatorType` enum
    - `shared/src/main/kotlin/com/klikcalendar/app/strings/Localization.kt`: Added `battery` string (English: "Battery", Chinese: "电量")
    - `shared/src/main/kotlin/com/klikcalendar/app/ui/components/StatusOverlay.kt`: Added battery icon (BatteryChargingFull) and comprehensive hardware integration documentation
    - `shared/src/main/kotlin/com/klikcalendar/app/state/SampleData.kt`: Added battery indicator to sample data (85%, Normal level)
  - Change: Added Battery indicator type to show device battery level, kept existing Bluetooth (BLE) indicator for connectivity status
  - Impact: Status bar now displays battery and bluetooth status alongside existing indicators (Recording, Sync). Prepared for future hardware integration.
  - Hardware Integration: Added detailed documentation in StatusOverlay.kt explaining how to integrate with platform-specific battery and BLE APIs (Android BatteryManager/BluetoothAdapter, iOS UIDevice/CoreBluetooth)

- 2025-10-29: Removed two header indicators to the right of Sync (Sensors and Network) from the status overlay UI.
  - File: `shared/src/commonMain/kotlin/com/klikcalendar/app/ui/components/StatusOverlay.kt`
  - Change: Filtered out `DeviceIndicatorType.Sensors` and `DeviceIndicatorType.Network` before rendering indicator pills.
  - Impact: The top header now shows only BLE, Recording, and Sync indicators; UI density is reduced and matches spec screenshot.

### Integration Notes
- Callers continue to populate `StatusOverlayState.indicators` as before. Rendering now ignores `Sensors` and `Network` types. No API changes.
- If future deployments require re-enabling these indicators (e.g., device health surfaces), remove the filter or make visibility configurable via state (e.g., feature flags fetched from backend or settings storage).

### Future DB/Config Integration
- Recommended to add a user/org-level UI-config table or remote config key to control indicator visibility per type. Example schema (conceptual):
  - Table: `ui_indicator_visibility`
    - Columns: `tenant_id`, `indicator_type`, `visible` (bool), `updated_at`.
- The Compose UI can read a resolved config map and filter dynamically.

### How to Use
- The overlay is composed via `StatusOverlay(state, strings, onProfile, onLanguageToggle)`.
- Provide `StatusOverlayState` from your view model. Hidden types (Sensors, Network) will be excluded automatically.
