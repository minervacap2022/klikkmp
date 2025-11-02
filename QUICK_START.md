# Quick Start Guide - Backend Integration

## ✅ What's Done

Your iOS app now has **complete backend integration** for audio recording and processing:

### Features Added
1. **🎙️ Audio Recording** - Tap the microphone button to record
2. **☁️ Automatic Upload** - Audio uploads to backend at `http://86.38.238.159:8000`
3. **⚙️ Backend Processing** - Triggers the complete pipeline:
   - ASR (Whisper transcription)
   - Speaker diarization
   - LLM processing (Qwen3-14B)
   - Todo extraction
   - Meeting minutes generation
   - Knowledge graph creation
4. **📊 Results Display** - Shows all processed data in 5 tabs:
   - Transcript (with timestamps & speakers)
   - Todos (with assignees & due dates)
   - Meeting Minutes
   - Participants (with stats)
   - Knowledge Graph (entities & relations)

---

## 🚀 How to Use

### Build and Run

```bash
# Navigate to project
cd "Klik_calendar_new"

# Clean and build
./gradlew clean build

# Open in Xcode
open Klik_calendar_new.xcodeproj

# In Xcode:
# 1. Select a simulator or device
# 2. Press ⌘R to build and run
```

### In the App

1. **Start Recording**
   - Tap the microphone FAB (floating action button)
   - Grant microphone permission when prompted
   - Speak for 10-30 seconds

2. **Stop Recording**
   - Tap the red stop button
   - Watch as it uploads and processes
   - Progress indicator shows status

3. **View Results**
   - Results screen opens automatically when done
   - Browse through 5 tabs of processed data
   - Tap "Back" to close

---

## 📁 What Was Added

### New Files (10)
```
shared/src/commonMain/kotlin/com/klikcalendar/app/
├── data/backend/BackendModels.kt         # API response models
├── data/backend/BackendApiClient.kt      # HTTP client
├── audio/AudioRecorder.kt                # Recording interface
├── state/RecordingViewModel.kt           # Recording logic
├── ui/components/RecordingButton.kt      # Animated button
├── ui/components/RecordingStatusIndicator.kt  # Status display
└── ui/screens/RecordingResultsScreen.kt  # Results UI

shared/src/iosMain/kotlin/com/klikcalendar/app/
└── audio/IOSAudioRecorder.kt             # iOS audio recording
```

### Modified Files (2)
```
shared/src/commonMain/kotlin/com/klikcalendar/app/App.kt  # Integration
Klik_calendar_new/Info.plist                              # Microphone permission
```

---

## 🔧 Configuration

### Backend URL
Default: `http://86.38.238.159:8000`

To change:
```kotlin
// In BackendApiClient.kt
class BackendApiClient(
    private val baseUrl: String = "YOUR_BACKEND_URL_HERE",
)
```

### Polling Interval
Default: 3 seconds

To change:
```kotlin
// In RecordingViewModel.kt, pollForResults()
backendClient.pollUntilComplete(
    runId = runId,
    pollIntervalMs = 3000,  // Change this
)
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| **Permission denied** | Go to Settings → Privacy → Microphone → Enable |
| **Upload fails** | Check internet & backend connectivity |
| **Processing timeout** | Verify backend is running and accessible |
| **Empty results** | Check backend logs, ensure audio has speech |
| **Build errors** | Run `./gradlew clean` and rebuild |

---

## 📝 Next Steps

1. **Build and test** the app
2. **Verify backend is running** at `http://86.38.238.159:8000`
3. **Test the complete flow** record → upload → process → view results
4. **Customize as needed** (colors, texts, backend URL)

---

## 📚 Documentation

- Full documentation: `BACKEND_INTEGRATION_COMPLETE.md`
- API reference: `sample_output/KK_frontendios/API_REFERENCE.md`
- Backend guide: `sample_output/KK_frontendios/BACKEND_INTEGRATION_GUIDE.md`

---

**Status**: ✅ Ready to Build and Test
**Integration**: ✅ Complete
**Date**: 2025-10-31
