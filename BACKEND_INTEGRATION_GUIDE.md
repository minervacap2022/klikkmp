# Backend Integration Guide

## Overview

All placeholder and sample data have been removed from the Klik Calendar app. The app is now ready for backend integration with production data.

## Changes Made

### 1. Removed All Placeholder Data

#### CalendarScreen.kt
- **Removed**: Hardcoded todo list items (6 Chinese text items)
- **Status**: Empty state ready for backend data

#### ProfileScreen.kt
- **Removed**:
  - Firmware version: "v2.1.3"
  - Device name: "Klik Device"
  - Recording statistics: 127次, 48小时23分钟, 89个
  - Storage: "23.4 GB / 64 GB"
  - Payment plan: "¥99/月"
- **Replaced with**: "--" placeholders ready for backend data

#### App.kt
- **Removed**: Debug `println` statement
- **Replaced with**: TODO comment for backend integration

#### AppState.kt
- **Removed**: All references to `SampleData`
- **Replaced with**: Empty lists and default state initialization
- **Added**: Comprehensive TODO comments with example implementation

### 2. Created Production-Ready Data Architecture

#### New Files Created

**`shared/src/commonMain/kotlin/com/klikcalendar/app/data/KlikRepository.kt`**
- Repository interface defining all data operations
- Documents expected backend endpoints for each operation
- Ready for implementation with HTTP client (e.g., Ktor)

**`shared/src/commonMain/kotlin/com/klikcalendar/app/data/KlikRepositoryImpl.kt`**
- Production implementation returning empty data
- Includes comprehensive TODO comments with API integration examples
- Each method documents the expected backend endpoint

### 3. Documentation Updates

**SampleData.kt**
- Added clear warning: "⚠️ DEVELOPMENT ONLY - DO NOT USE IN PRODUCTION ⚠️"
- Documented that this data is NOT used in production
- Kept for development/testing purposes only

## Backend Integration Steps

### Step 1: Add HTTP Client Dependency

Add Ktor client to `shared/build.gradle.kts`:

```kotlin
val commonMain by getting {
    dependencies {
        // Existing dependencies...
        implementation("io.ktor:ktor-client-core:2.3.7")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    }
}

val iosMain by getting {
    dependencies {
        // Existing dependencies...
        implementation("io.ktor:ktor-client-darwin:2.3.7")
    }
}
```

### Step 2: Implement API Client

Update `KlikRepositoryImpl.kt`:

```kotlin
class KlikRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : KlikRepository {

    override suspend fun getTimeline(): List<TimelineDay> {
        return httpClient.get("$baseUrl/api/v1/timeline").body()
    }

    override suspend fun getStatusOverlay(): StatusOverlayState {
        return httpClient.get("$baseUrl/api/v1/status").body()
    }

    // Implement other methods...
}
```

### Step 3: Add Data Models for API Serialization

Create DTOs (Data Transfer Objects) for API communication:

```kotlin
// Example DTO
@Serializable
data class TimelineDayDto(
    val date: String, // ISO date format
    val focus: Boolean,
    val utilization: Int,
    val events: List<CalendarEventDto>
)

// Map DTOs to domain models
fun TimelineDayDto.toDomain(): TimelineDay { ... }
```

### Step 4: Initialize Repository in App

Update app initialization to use the repository:

```kotlin
@Composable
fun KlikApp() {
    val repository = remember {
        KlikRepositoryImpl(
            httpClient = createHttpClient(),
            baseUrl = "https://api.klikcalendar.com"
        )
    }

    val state = rememberKlikAppState(repository)
    // ... rest of app
}
```

### Step 5: Load Data Asynchronously

Implement data loading in `rememberKlikAppState`:

```kotlin
@Composable
fun rememberKlikAppState(
    repository: KlikRepository,
    initialLanguage: Language = Language.Chinese,
): KlikAppState {
    val state = remember {
        KlikAppState(
            language = initialLanguage,
            // ... empty initial state
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val timeline = repository.getTimeline()
                state.timeline = timeline

                val overlay = repository.getStatusOverlay()
                state.overlayState = overlay

                // Load other data...
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    return state
}
```

## Expected Backend API Endpoints

All endpoints are documented in `KlikRepository.kt`:

### Status & Device
- `GET /api/v1/status` - Get status overlay state
- `GET /api/v1/device/info` - Get device information for profile

### Calendar & Events
- `GET /api/v1/timeline?startDate=...&endDate=...` - Get timeline data
- `GET /api/v1/transcripts` - Get transcript records
- `GET /api/v1/memos` - Get meeting memos

### Knowledge & Operations
- `GET /api/v1/knowledge-graph` - Get knowledge graph nodes
- `GET /api/v1/operations` - Get operational tasks
- `GET /api/v1/quick-actions` - Get available quick actions

### Feedback
- `GET /api/v1/feedback/pending` - Get pending feedback items
- `POST /api/v1/feedback/{itemId}` - Submit feedback

### User & Subscription
- `GET /api/v1/subscription` - Get user subscription info

## Testing Recommendations

1. **Unit Tests**: Test repository implementations with mock HTTP client
2. **Integration Tests**: Test against staging backend API
3. **Error Handling**: Test network failures, timeout scenarios
4. **Loading States**: Implement loading indicators in UI
5. **Offline Support**: Consider caching strategy for offline access

## Current State

- ✅ All placeholder data removed
- ✅ Clean architecture with repository pattern
- ✅ Empty state initialization
- ✅ Comprehensive documentation
- ✅ Production-ready structure
- ⏳ Backend API integration pending
- ⏳ HTTP client configuration pending
- ⏳ Error handling & loading states pending

## Notes

- The app will show empty states until backend integration is complete
- `SampleData.kt` is preserved for development/testing but NOT used in production
- All UI components are data-driven and will work once repository returns data
- Profile screen shows "--" for all values until device info API is connected
