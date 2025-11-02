# Backend Integration Guide for iOS App

## Overview
This document outlines what the backend needs to provide for the iOS app frontend to connect and display pipeline results.

## Current Backend Status

Your backend API already exists at `/root/KK_frontendweb/backend/pipeline_api.py` with the following endpoints:

### 1. **Audio Upload & Pipeline Execution**
**Endpoint:** `POST /api/pipeline/execute`

**Request:**
- Content-Type: `multipart/form-data`
- Fields:
  - `file`: Audio file (required)
  - `session_id`: Optional session ID (string)
  - `enable_preprocessing`: "True" or "False" (string)

**Response (200 OK):**
```json
{
  "sessionId": "uuid-string",
  "status": "started",
  "message": "Pipeline execution started",
  "runId": "uuid-string"
}
```

**What to ensure:**
- ? Accepts audio file uploads (already implemented)
- ? Returns `runId` immediately (already implemented)
- ? Processes pipeline asynchronously (already implemented)
- ? CORS enabled for mobile apps (already implemented - allows all origins)

### 2. **Check Pipeline Status**
**Endpoint:** `GET /api/pipeline/status/{run_id}`

**Response:**
```json
{
  "runId": "uuid",
  "sessionId": "uuid",
  "status": "RUNNING" | "COMPLETED" | "FAILED",
  "startTime": "2025-01-XX...",
  "inputFile": "/path/to/file",
  "logs": ["[timestamp] log message"],
  "modules": [...],
  "dropboxLinks": [...],
  "databaseStats": {...},
  "error": null | "error message",
  "endTime": "2025-01-XX...",
  "executionTime": 123.45,
  "completeResult": {...},
  "frontendData": {...}
}
```

**What to ensure:**
- ? Returns status in real-time (already implemented)
- ? When `status == "COMPLETED"`, `completeResult` and `frontendData` are populated (already implemented)

### 3. **Stream Real-Time Logs**
**Endpoint:** `GET /api/pipeline/logs/{run_id}`

**Response:** Server-Sent Events (SSE) stream
- Format: `data: {"type": "log", "message": "..."}\n\n`

**What to ensure:**
- ? SSE endpoint working (already implemented)
- ? iOS app can connect to SSE endpoint (may need testing)

### 4. **List All Runs**
**Endpoint:** `GET /api/pipeline/runs`

**Response:**
```json
{
  "runs": [...],
  "total_runs": 5
}
```

## Data Structure Mapping

The backend returns data in TWO formats:
1. **`completeResult`** - Raw pipeline output (detailed, technical)
2. **`frontendData`** - Optimized frontend format (recommended for iOS app)

**RECOMMENDATION: Use `frontendData` when available**, as it's structured specifically for frontend consumption.

### 1. **Transcripts** 
**Preferred Source:** `frontendData.transcript.segments`

**Format in `frontendData`:**
```json
{
  "transcript": {
    "segments": [...],  // Array of transcript segments
    "total_segments": 103,
    "total_duration": 1234.5,
    "speakers_detected": 3,
    "language": "en",
    "optimization_method": "...",
    ...
  }
}
```

**Alternative Source:** `completeResult.asr_result.segments` or `completeResult.optimized_segments`

**Segment Format:**
```json
{
  "start": 0.0,
  "end": 2.0,
  "text": "Speaker text here",
  "speaker": "SPEAKER_00",
  "speaker_name": "Tripp",
  "speaker_profile_id": "uuid",
  "speaker_user_id": "SPEAKER_00"
}
```

**Frontend needs:**
- List of transcript segments with timestamps
- Speaker names/IDs
- Time-ordered display

### 2. **Todos / Tasks**
**Preferred Source:** `frontendData.todos.items`

**Format in `frontendData`:**
```json
{
  "todos": {
    "items": [...],  // Array of todo objects
    "total_count": 5,
    "extraction_method": "...",
    ...
  }
}
```

**Alternative Source:** `completeResult.todos` (direct array)

**Todo Item Format:**
```json
{
  "id": "todo-1",
  "text": "Task description",
  "assignee": "Tripp",
  "due_date": "2025-10-31",
  "status": "pending",
  "category": "C",
  "priority": "high",
  "assignee_profile_id": "uuid",
  "assignee_user_id": "SPEAKER_00",
  "assignee_match_confidence": "new_profile",
  "timestamp": 0.0
}
```

**Frontend needs:**
- List of todos with assignees
- Status, category, priority, due dates
- Filterable by assignee, status, category

### 3. **Meeting Minutes / Memos**
**Preferred Source:** `frontendData.meeting_minutes`

**Format:**
- Check structure - may be dict with content field, or string with markdown

**Alternative Source:** `completeResult.meeting_minutes` (usually markdown string)

**Content:**
```
**Comprehensive Meeting Minutes**

### 1. Meeting Topic and Objectives
...

### 2. Main Discussion Points
...
```

**Frontend needs:**
- Display markdown as formatted text
- Render sections, headings, action items

### 4. **Knowledge Graph**
**Source:** `completeResult.kg_entities` and `completeResult.kg_relations`

**OR:** `frontendData.knowledge_graph` (check structure)

**Format:**
```json
{
  "kg_entities": [
    {
      "id": "entity_0",
      "type": "PERSON",
      "name": "SPEAKER_01",
      "confidence": 0.95
    }
  ],
  "kg_relations": [
    {
      "from": "entity_0",
      "to": "entity_1",
      "type": "RELATED_TO",
      "weight": 0.8
    }
  ]
}
```

**Frontend needs:**
- Graph visualization (nodes = entities, edges = relations)
- Entity types (PERSON, ORGANIZATION, etc.)
- Filterable by entity type

### 5. **Participants**
**Preferred Source:** `frontendData.participants`

**Format:**
```json
{
  "participants": {
    "speaker_id": {
      "name": "Tripp",
      "profile_id": "uuid",
      "duration": 123.45,
      "speech_segments": 10
    }
  }
}
```

**Alternative:** `completeResult.participants` or `completeResult.speaker_map`

**Frontend needs:**
- List of meeting participants
- Speaker activity/stats

## Backend Requirements Summary

### ? Already Implemented:
1. File upload endpoint (`POST /api/pipeline/execute`)
2. Status checking endpoint (`GET /api/pipeline/status/{run_id}`)
3. Log streaming endpoint (`GET /api/pipeline/logs/{run_id}`)
4. Pipeline execution in background
5. CORS enabled for all origins
6. Data persistence in `completeResult` and `frontendData`

### ? Needs Verification/Testing:
1. **CORS Configuration**: Ensure iOS app can make requests
   - Current: `allow_origins=["*"]` ? Should work
   - May need to test from iOS app

2. **File Format Support**: Ensure backend accepts iOS audio formats
   - Common iOS formats: `.m4a`, `.caf`, `.wav`, `.mp3`
   - Backend currently accepts any file type ?

3. **Response Format Consistency**: Ensure `frontendData` is always populated
   - Check that `frontendData` contains:
     - `transcript` (dict or segments)
     - `meeting_minutes` (dict or string)
     - `todos` (dict with todo objects)
     - `participants` (dict)
     - `knowledge_graph` (dict with nodes/edges)

4. **Error Handling**: Ensure errors are returned as JSON
   - Current: ? Error handler returns JSON

5. **Timeout Configuration**: 
   - Pipeline may take time - ensure iOS app can handle long-running requests
   - Status polling recommended instead of waiting for completion

## Recommended iOS App Integration Flow

1. **Record Audio** ¡ú iOS app records audio
2. **Upload Audio** ¡ú `POST /api/pipeline/execute` with audio file
3. **Get runId** ¡ú Extract `runId` from response
4. **Poll Status** ¡ú Periodically call `GET /api/pipeline/status/{runId}` 
   - Or use SSE: `GET /api/pipeline/logs/{runId}`
5. **When Status = "COMPLETED"** ¡ú Extract data:
   - Transcripts: `response.completeResult.asr_result.segments` or `response.frontendData.transcript`
   - Todos: `response.completeResult.todos` or `response.frontendData.todos`
   - Meeting Minutes: `response.completeResult.meeting_minutes` or `response.frontendData.meeting_minutes`
   - Knowledge Graph: `response.completeResult.kg_entities` + `kg_relations`
6. **Display Results** ¡ú Map to UI sections

## Backend Testing Checklist

Before iOS app connects, verify:

- [ ] `POST /api/pipeline/execute` accepts audio files
- [ ] Response includes `runId` immediately
- [ ] `GET /api/pipeline/status/{runId}` returns status updates
- [ ] When completed, `completeResult` and `frontendData` are populated
- [ ] CORS allows requests from iOS app (test with curl/postman)
- [ ] Error responses are JSON (not HTML)
- [ ] SSE endpoint works for log streaming (test with curl)

## Sample Test Commands

```bash
# Test upload
curl -X POST http://86.38.238.159:8000/api/pipeline/execute \
  -F "file=@test_audio.wav" \
  -F "session_id=test-123"

# Test status (replace RUN_ID)
curl http://86.38.238.159:8000/api/pipeline/status/RUN_ID

# Test SSE logs (replace RUN_ID)
curl -N http://86.38.238.159:8000/api/pipeline/logs/RUN_ID
```

## Important Notes

1. **Data Structure**: The backend already provides both `completeResult` (raw) and `frontendData` (processed). The iOS app should use `frontendData` when available, as it's optimized for frontend consumption.

2. **Polling vs SSE**: 
   - Status polling: Simple, works everywhere
   - SSE: Real-time logs, better UX, but requires SSE support in iOS HTTP client

3. **Frontend Data Format**: Check `frontendData` structure - it may organize todos, transcripts, etc. differently than `completeResult`.

4. **Error Handling**: All errors return JSON with structure:
   ```json
   {
     "runId": "...",
     "status": "FAILED",
     "error": "error message"
   }
   ```

5. **Backend URL**: The iOS app will need to know the backend URL:
   - Current: `http://86.38.238.159:8000`
   - Or configure via environment/settings

## Next Steps for Backend Team

1. **Verify CORS works from mobile devices** (test with actual iOS app or Postman with iOS User-Agent)
2. **Ensure `frontendData` is always populated** with expected structure
3. **Test with sample audio files** to ensure pipeline completes successfully
4. **Document API base URL** for iOS team
5. **Consider adding authentication** if needed (currently open)
6. **Add rate limiting** if necessary to prevent abuse

---

**Status**: Backend API is already fully implemented! Just needs testing and verification that it works with iOS app requests.

