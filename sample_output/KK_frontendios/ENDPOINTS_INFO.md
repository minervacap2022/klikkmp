# Backend API Endpoints - Quick Reference for iOS

## 🔗 Server Information

**Backend Server IP**: `86.38.238.159`  
**Port**: `8000`  
**Base URL**: `http://86.38.238.159:8000`

## ✅ Authentication

**NO AUTHENTICATION REQUIRED**  
- No API keys
- No tokens
- No authentication headers needed
- Open access (CORS enabled for all origins)

## 📍 Endpoints

### 1. Upload Audio & Start Pipeline
**URL**: `http://86.38.238.159:8000/api/pipeline/execute`  
**Method**: `POST`  
**Content-Type**: `multipart/form-data`

**Parameters**:
- `file` (required): Audio file
- `session_id` (optional): Session ID string
- `enable_preprocessing` (optional): "True" or "False"

**Response**:
```json
{
  "sessionId": "uuid",
  "status": "started",
  "message": "Pipeline execution started",
  "runId": "uuid"
}
```

### 2. Check Pipeline Status
**URL**: `http://86.38.238.159:8000/api/pipeline/status/{runId}`  
**Method**: `GET`

**Response**: Status object with `frontendData` when completed

### 3. Stream Logs (SSE)
**URL**: `http://86.38.238.159:8000/api/pipeline/logs/{runId}`  
**Method**: `GET`  
**Format**: Server-Sent Events

### 4. List All Runs
**URL**: `http://86.38.238.159:8000/api/pipeline/runs`  
**Method**: `GET`

### 5. Health Check
**URL**: `http://86.38.238.159:8000/api/pipeline/health`  
**Method**: `GET`

## 🚀 Quick Start for iOS

1. **Upload Audio**:
   ```
   POST http://86.38.238.159:8000/api/pipeline/execute
   Content-Type: multipart/form-data
   
   Body: file=<audio_data>, session_id=<optional>
   ```

2. **Get runId** from response

3. **Poll Status**:
   ```
   GET http://86.38.238.159:8000/api/pipeline/status/{runId}
   ```

4. **When status == "COMPLETED"**, extract:
   - `response.frontendData.transcript.segments`
   - `response.frontendData.todos.items`
   - `response.frontendData.meeting_minutes.content`
   - `response.frontendData.participants.items`
   - `response.completeResult.kg_entities`

## ⚠️ Important Notes

- No authentication required
- CORS is enabled (all origins allowed)
- Pipeline runs asynchronously (poll status, don't wait)
- Results available when `status == "COMPLETED"`
