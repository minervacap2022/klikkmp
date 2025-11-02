# Backend API Reference for iOS App

## Base URL
- **Local Development**: `http://localhost:8000`
- **Production/Remote**: `http://86.38.238.159:8000`

## Endpoints

### 1. Health Check
**GET** `/api/pipeline/health`

Check if the backend API is running.

**Response:**
```json
{
  "status": "healthy",
  "timestamp": "2025-10-31T12:12:07.031982",
  "active_runs": 0
}
```

---

### 2. Upload Audio & Start Pipeline
**POST** `/api/pipeline/execute`

Upload an audio file to start the processing pipeline.

**Content-Type:** `multipart/form-data`

**Parameters:**
- `file` (required): Audio file (`.m4a`, `.wav`, `.mp3`, `.caf`, etc.)
- `session_id` (optional): Session ID string
- `enable_preprocessing` (optional): `"True"` or `"False"` (default: `"True"`)

**Example Request:**
```swift
// Swift example
let url = URL(string: "http://86.38.238.159:8000/api/pipeline/execute")!
var request = URLRequest(url: url)
request.httpMethod = "POST"

let boundary = UUID().uuidString
request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

var body = Data()
body.append("--\(boundary)\r\n".data(using: .utf8)!)
body.append("Content-Disposition: form-data; name=\"file\"; filename=\"recording.m4a\"\r\n".data(using: .utf8)!)
body.append("Content-Type: audio/m4a\r\n\r\n".data(using: .utf8)!)
body.append(audioData)
body.append("\r\n--\(boundary)--\r\n".data(using: .utf8)!)

request.httpBody = body
```

**Response (200 OK):**
```json
{
  "sessionId": "uuid-string",
  "status": "started",
  "message": "Pipeline execution started",
  "runId": "uuid-string"
}
```

**Error Response (400/500):**
```json
{
  "runId": "uuid-string",
  "sessionId": "uuid-string",
  "status": "FAILED",
  "error": "Error message here",
  "message": "Error description"
}
```

---

### 3. Get Pipeline Status
**GET** `/api/pipeline/status/{runId}`

Get the current status and results of a pipeline run.

**Parameters:**
- `runId` (path): The run ID returned from the upload endpoint

**Response (200 OK):**

**While Running:**
```json
{
  "runId": "uuid",
  "sessionId": "uuid",
  "status": "RUNNING",
  "startTime": "2025-10-31T12:02:28.450948",
  "inputFile": "/path/to/file",
  "logs": ["[timestamp] log message"],
  "modules": [...],
  "dropboxLinks": [],
  "databaseStats": {},
  "error": null,
  "endTime": null,
  "executionTime": null,
  "completeResult": null,
  "frontendData": null
}
```

**When Completed:**
```json
{
  "runId": "uuid",
  "sessionId": "uuid",
  "status": "COMPLETED",
  "startTime": "2025-10-31T12:02:28.450948",
  "endTime": "2025-10-31T12:05:30.123456",
  "executionTime": 181.67,
  "completeResult": {
    // Full pipeline results (see Data Structures section)
  },
  "frontendData": {
    // Frontend-optimized data (see Data Structures section)
  }
}
```

**Error Response:**
```json
{
  "runId": "uuid",
  "status": "FAILED",
  "error": "Error message",
  ...
}
```

---

### 4. Stream Real-Time Logs (SSE)
**GET** `/api/pipeline/logs/{runId}`

Stream real-time logs using Server-Sent Events.

**Parameters:**
- `runId` (path): The run ID

**Response:** Server-Sent Events stream

**Format:**
```
data: {"type": "log", "message": "[timestamp] Log message"}

data: {"type": "heartbeat"}

data: {"type": "complete", "status": "COMPLETED"}

```

**iOS Implementation Note:**
iOS URLSession supports SSE streams. Example:
```swift
let url = URL(string: "http://86.38.238.159:8000/api/pipeline/logs/\(runId)")!
let task = URLSession.shared.dataTask(with: url)
// Handle stream data...
```

---

### 5. List All Runs
**GET** `/api/pipeline/runs`

Get a list of all pipeline runs (for history/previous runs view).

**Response (200 OK):**
```json
{
  "runs": [
    {
      "runId": "uuid",
      "sessionId": "uuid",
      "status": "COMPLETED",
      "startTime": "2025-10-31T12:02:28.450948",
      ...
    }
  ],
  "total_runs": 1
}
```

---

## Data Structures

### frontendData Structure

This is the **recommended** data source for iOS app.

```json
{
  "session_id": "uuid",
  "timestamp": "2025-10-31T12:02:28.450948",
  "transcript": {
    "segments": [
      {
        "start": 0.0,
        "end": 2.0,
        "text": "Speaker text",
        "speaker": "SPEAKER_00",
        "speaker_name": "Tripp",
        "speaker_profile_id": "uuid",
        "speaker_user_id": "SPEAKER_00"
      }
    ],
    "total_segments": 103,
    "total_duration": 1234.5,
    "speakers_detected": 3,
    "language": "en"
  },
  "todos": {
    "items": [
      {
        "id": "todo-1",
        "text": "Task description",
        "assignee": "Tripp",
        "due_date": "2025-10-31",
        "status": "pending",
        "category": "C",
        "priority": "high",
        "timestamp": 0.0
      }
    ],
    "total_count": 5
  },
  "meeting_minutes": {
    "content": "**Meeting Minutes**\n\n### 1. Topic...",
    "generated_at": "2025-10-31T12:02:28.450948"
  },
  "participants": {
    "items": [
      {
        "name": "Tripp",
        "profile_id": "uuid",
        "duration": 123.45,
        "speech_segments": 10
      }
    ],
    "total_count": 3
  },
  "knowledge_graph": {
    "entities": [...],
    "total_entities": 3
  }
}
```

### completeResult Structure

Raw pipeline output (use as fallback if frontendData missing).

```json
{
  "session_id": "uuid",
  "asr_result": {
    "segments": [...],
    "num_speakers": 3
  },
  "todos": [
    {
      "id": "todo-1",
      "text": "Task",
      "assignee": "Tripp",
      ...
    }
  ],
  "meeting_minutes": "**Meeting Minutes**\n\n...",
  "kg_entities": [
    {
      "id": "entity_0",
      "type": "PERSON",
      "name": "SPEAKER_01"
    }
  ],
  "kg_relations": [
    {
      "from": "entity_0",
      "to": "entity_1",
      "type": "RELATED_TO"
    }
  ]
}
```

## Error Handling

All errors return JSON with this structure:

```json
{
  "runId": "uuid",
  "sessionId": "uuid",
  "status": "FAILED",
  "error": "Error message",
  "message": "Detailed error description"
}
```

Common HTTP status codes:
- `200`: Success (even for failures - check `status` field)
- `400`: Bad request (missing file, invalid parameters)
- `404`: Run not found
- `500`: Internal server error

## iOS Integration Checklist

- [ ] Configure base URL (local or remote)
- [ ] Implement audio recording
- [ ] Implement file upload with multipart/form-data
- [ ] Parse response to get `runId`
- [ ] Implement status polling (every 2-5 seconds)
- [ ] Handle status: RUNNING, COMPLETED, FAILED
- [ ] Extract `frontendData` when completed
- [ ] Map data to UI sections:
  - [ ] Transcripts ¡ú `frontendData.transcript.segments`
  - [ ] Todos ¡ú `frontendData.todos.items`
  - [ ] Meeting Minutes ¡ú `frontendData.meeting_minutes.content`
  - [ ] Participants ¡ú `frontendData.participants.items`
  - [ ] Knowledge Graph ¡ú `completeResult.kg_entities` + `kg_relations`
- [ ] Handle errors gracefully
- [ ] Show loading/processing state
- [ ] Optionally implement SSE log streaming

## Testing

Use the verification script:
```bash
python3 /root/KK_frontendios@KK_frontendios/verify_integration.py
```

Or test manually:
```bash
# Health check
curl http://86.38.238.159:8000/api/pipeline/health

# List runs
curl http://86.38.238.159:8000/api/pipeline/runs

# Upload (replace with actual file)
curl -X POST http://86.38.238.159:8000/api/pipeline/execute \
  -F "file=@recording.m4a" \
  -F "session_id=test-123"
```

---

**Last Updated**: 2025-10-31
**Backend Version**: 1.0.0

