# API 端点文档 - 移动端调用指南

---

## 2. 管道状态与监控端点

### 2.1 实时日志流（SSE）

**端点**: `GET /api/pipeline/logs/{run_id}`

**功能**: 通过 Server-Sent Events (SSE) 实时接收管道执行日志。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `run_id` | String | 运行 ID（从 `/execute` 响应获取） |

**响应格式**: SSE 事件流

**事件类型**:
1. **log**: 日志消息
   ```json
   {"type": "log", "message": "[10:36:54] Starting pipeline execution..."}
   ```

2. **heartbeat**: 心跳包（每 30 秒）
   ```json
   {"type": "heartbeat"}
   ```

3. **complete**: 处理完成
   ```json
   {"type": "complete", "status": "COMPLETED"}
   ```

4. **error**: 错误事件
   ```json
   {"type": "error", "message": "Stage4Pipeline.__init__() got an unexpected keyword argument"}
   ```

---

### 2.2 查询运行状态

**端点**: `GET /api/pipeline/status/{run_id}`

**功能**: 获取指定运行的当前状态、执行进度和结果信息。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `run_id` | String | 运行 ID |

**响应示例**:
```json
{
  "runId": "04523c53-1e83-4bb6-a274-17667d3f49bf",
  "sessionId": "session_1762425403061_ra2duqssw",
  "status": "COMPLETED",
  "startTime": "2025-11-06T10:36:54.815268",
  "endTime": "2025-11-06T10:41:07.328646",
  "executionTime": 252.513,
  "inputFile": "/tmp/klik_uploads/04523c53_audio.mp3",
  "logs": ["[10:36:54] Starting...", "..."],
  "modules": ["Stage1", "Stage2", "Stage3"],
  "dropboxLinks": [
    "https://www.dropbox.com/s/xxx/SESSION_xxx_results.json"
  ],
  "databaseStats": {
    "milvus": {"vectors_stored": 150},
    "postgresql": {"entities_stored": 45},
    "redis": {"cache_hits": 89}
  },
  "error": null,
  "completeResult": {
    "stage1_output": "/path/to/SESSION_xxx_results.json",
    "stage5_output": "/path/to/SESSION_xxx_meetingminutes.json"
  },
  "frontendData": {
    "participants": [...],
    "duration": "17:49"
  }
}
```

**响应字段说明**:
- `status`: 运行状态
  - `RUNNING`: 正在执行
  - `COMPLETED`: 成功完成
  - `FAILED`: 执行失败
- `executionTime`: 执行耗时（秒）
- `modules`: 已完成的处理阶段列表
- `dropboxLinks`: 结果文件的 Dropbox 下载链接（如已配置）
- `databaseStats`: 数据库存储统计
- `completeResult`: 完整结果对象，包含各阶段输出文件路径
- `frontendData`: 前端展示用的简化数据

---

### 2.3 获取所有运行记录

**端点**: `GET /api/pipeline/runs`

**功能**: 获取所有管道运行记录的列表，包括历史记录。

**响应示例**:
```json
{
  "runs": [
    {
      "runId": "04523c53-1e83-4bb6-a274-17667d3f49bf",
      "sessionId": "session_1762425403061_ra2duqssw",
      "status": "COMPLETED",
      "startTime": "2025-11-06T10:36:54.815268",
      "inputFile": "audio.mp3"
    },
    {
      "runId": "36b27499-2dca-44b7-ad57-9676694a9264",
      "sessionId": "session_1762426789123_xyz",
      "status": "RUNNING",
      "startTime": "2025-11-06T11:15:32.456789",
      "inputFile": "meeting_recording.m4a"
    }
  ],
  "total_runs": 2
}
```

---

### 2.4 健康检查

**端点**: `GET /api/pipeline/health`

**功能**: 检查 API 服务是否正常运行。

**响应示例**:
```json
{
  "status": "healthy",
  "timestamp": "2025-11-06T12:00:00.123456Z",
  "active_runs": 3
}
```

---

## 3. 流式会话管理端点

### 3.1 查询流状态

**端点**: `GET /api/pipeline/stream-status/{stream_id}`

**功能**: 获取持续录音流的当前状态和统计信息。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `stream_id` | String | 流标识符（从 `/stream-continuous` 获取） |

**响应示例**:
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "active": true,
  "current_session_id": "SESSION_20251111_183628_3e9b8d3a",
  "package_stats": {
    "total_packages": 15420,
    "valid_packages": 15418,
    "invalid_packages": 2
  },
  "boundary_stats": {
    "sessions_completed": 3,
    "boundaries_detected": 3,
    "current_session_id": "SESSION_20251111_183628_3e9b8d3a"
  }
}
```

**响应字段说明**:
- `active`: 流是否仍在运行
- `current_session_id`: 当前会话 ID
- `package_stats`: 数据包统计
  - `total_packages`: 总包数
  - `valid_packages`: 有效包数
  - `invalid_packages`: 无效包数
- `boundary_stats`: 边界检测统计
  - `sessions_completed`: 已完成的会话数
  - `boundaries_detected`: 检测到的边界数

---

### 3.2 停止录音流

**端点**: `DELETE /api/pipeline/stream-stop/{stream_id}`

**功能**: 停止持续录音流并保存当前会话。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `stream_id` | String | 流标识符 |

**响应示例**:
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "status": "stopped",
  "final_session": {
    "session_id": "SESSION_20251111_183628_3e9b8d3a",
    "duration": 1845.6,
    "audio_file": "/path/to/session_audio.wav"
  }
}
```

---

### 3.3 强制触发会话边界（测试用）

**端点**: `POST /api/pipeline/force-boundary/{stream_id}`

**功能**: 手动触发会话边界，用于测试边界检测逻辑。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `stream_id` | String | 流标识符 |

**响应示例**:
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "status": "boundary_forced",
  "session": {
    "session_id": "SESSION_20251111_183628_3e9b8d3a",
    "forced": true
  }
}
```

---

### 3.4 动态切换检测模式

**端点**: `PUT /api/pipeline/set-detection-mode/{stream_id}`

**功能**: 在录音过程中动态调整会话边界检测的灵敏度。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `stream_id` | String | 流标识符 |

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `mode` | String | 是 | 检测模式：`aggressive`, `balanced`, `conservative` |

**请求示例**:
```
PUT /api/pipeline/set-detection-mode/stream_1762858780394_vfqamg?mode=aggressive
```

**响应示例**:
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "mode": "aggressive",
  "status": "mode_changed"
}
```

---

### 3.5 导出检测指标

**端点**: `GET /api/pipeline/export-metrics/{stream_id}`

**功能**: 导出会话边界检测的历史指标数据，用于分析和调优。

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| `stream_id` | String | 流标识符 |

**查询参数**:
| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `format` | String | "json" | 导出格式：`json` 或 `csv` |

**响应格式**: 根据 `format` 参数返回 JSON 或 CSV 文件

**JSON 响应示例**:
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "metrics": [
    {
      "timestamp_ms": 1635789000000,
      "vad_probability": 0.85,
      "energy": 0.62,
      "zero_crossing_rate": 0.34,
      "boundary_confidence": 0.12
    }
  ]
}
```

---

## 4. 知识图谱端点

### 4.1 获取知识图谱节点

**端点**: `GET /api/knowledge-graph/nodes`

**功能**: 获取从会议中提取的实体节点（人物、公司、项目）。

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `type` | String | 否 | 过滤节点类型：`person`, `company`, `project` |

**请求示例**:
```
GET /api/knowledge-graph/nodes?type=person
```

**响应示例**:
```json
[
  {
    "id": "VP_49c4f59777d336c9",
    "label": "Beth",
    "type": "person",
    "x": 150.5,
    "y": 200.3
  },
  {
    "id": "PROJ_b2a3f7fae9be08f7",
    "label": "Free User Initiative",
    "type": "project",
    "x": 300.0,
    "y": 150.0
  }
]
```

**响应字段说明**:
- `id`: 实体唯一标识符
- `label`: 实体显示名称
- `type`: 实体类型（`person`, `company`, `project`）
- `x`, `y`: 图可视化坐标（可选）

---

### 4.2 获取知识图谱边

**端点**: `GET /api/knowledge-graph/edges`

**功能**: 获取实体之间的关系（如"某人在某公司工作"、"某人负责某项目"）。

**响应示例**:
```json
[
  {
    "from": "VP_49c4f59777d336c9",
    "to": "PROJ_b2a3f7fae9be08f7",
    "weight": 0.85,
    "label": "leads"
  },
  {
    "from": "VP_c3a533d37e5886ca",
    "to": "COM_GitLab_001",
    "weight": 0.92,
    "label": "works_for"
  }
]
```

**响应字段说明**:
- `from`: 源节点 ID
- `to`: 目标节点 ID
- `weight`: 关系置信度（0-1）
- `label`: 关系类型（`works_for`, `leads`, `assigned_to`, `project_belongs_to`）

---

## 5. WebSocket 实时流端点

### 5.1 实时音频流（录音模式）

**端点**: `WebSocket /api/pipeline/stream`

**功能**: 通过 WebSocket 从浏览器或移动设备实时传输 PCM 音频数据。

**连接参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `mode` | String | 是 | 固定为 `record` |
| `sessionId` | String | 否 | 会话 ID |

**连接示例 (Swift)**:
```swift
let url = URL(string: "ws://your-server:port/api/pipeline/stream?mode=record&sessionId=session_123")!
let webSocket = URLSession.shared.webSocketTask(with: url)
webSocket.resume()

// 发送音频数据
let audioData = Data([...]) // PCM 音频字节
webSocket.send(.data(audioData)) { error in
    if let error = error {
        print("Error sending audio:", error)
    }
}
```

**服务器发送的消息类型**:
1. **status**: 连接状态
   ```json
   {"type": "status", "data": "Connected successfully"}
   ```

2. **log**: 处理日志
   ```json
   {"type": "log", "data": "Processing audio chunk..."}
   ```

3. **error**: 错误信息
   ```json
   {"type": "error", "data": "Invalid audio format"}
   ```

---

### 5.2 测试模式 WebSocket（含实时指标）

**端点**: `WebSocket /api/pipeline/stream-test`

**功能**: 测试模式的 WebSocket 连接，返回实时的 VAD（语音活动检测）和边界检测指标。

**初始配置消息** (客户端发送):
```json
{
  "stream_id": "stream_1762858780394_vfqamg",
  "mode": "balanced"
}
```

**服务器发送的消息类型**:
1. **connected**: 连接成功
   ```json
   {"type": "connected", "stream_id": "stream_xxx", "mode": "balanced"}
   ```

2. **vad_update**: VAD 概率更新
   ```json
   {"type": "vad_update", "timestamp_ms": 1635789123456, "vad_prob": 0.85}
   ```

3. **boundary_detected**: 检测到会话边界
   ```json
   {
     "type": "boundary_detected",
     "timestamp_ms": 1635789234567,
     "confidence": 0.92,
     "trigger": "long_silence",
     "vad_prob": 0.02
   }
   ```

4. **mode_changed**: 检测模式已切换
   ```json
   {"type": "mode_changed", "mode": "aggressive"}
   ```

5. **error**: 错误
   ```json
   {"type": "error", "message": "Stream not found"}
   ```

---

## 6. 错误处理

### 6.1 HTTP 错误码

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 400 | 请求参数错误 | 检查参数格式和必填项 |
| 404 | 资源未找到 | 确认 `run_id` 或 `stream_id` 是否正确 |
| 500 | 服务器内部错误 | 查看日志，联系管理员 |
| 503 | 服务不可用 | 等待服务恢复或重试 |

### 6.2 错误响应格式

```json
{
  "detail": "No file uploaded",
  "status_code": 400
}
```

### 6.3 管道执行失败

当管道执行失败时，`/api/pipeline/status/{run_id}` 会返回：
```json
{
  "status": "FAILED",
  "error": "Stage4Pipeline.__init__() got an unexpected keyword argument 'llm_path'"
}
```

**处理建议**:
1. 检查 `error` 字段获取具体错误信息
2. 查看 `logs` 数组中的详细日志
3. 根据错误类型决定是否重试

---

## 8. 附录

### 8.1 完整端点清单

| 端点 | 方法 | 功能 |
|------|------|------|
| `/api/pipeline/execute` | POST | 执行完整管道 |
| `/api/pipeline/upload-lc3` | POST | LC3 音频上传 |
| `/api/pipeline/stream-file` | POST | 流式文件处理 |
| `/api/pipeline/stream-continuous` | POST | 启动连续录音 |
| `/api/pipeline/process-speechbrain` | POST | SpeechBrain 处理 |
| `/api/pipeline/logs/{run_id}` | GET | 实时日志（SSE） |
| `/api/pipeline/status/{run_id}` | GET | 查询运行状态 |
| `/api/pipeline/runs` | GET | 获取所有运行 |
| `/api/pipeline/health` | GET | 健康检查 |
| `/api/pipeline/stream-status/{stream_id}` | GET | 流状态查询 |
| `/api/pipeline/stream-stop/{stream_id}` | DELETE | 停止录音流 |
| `/api/pipeline/force-boundary/{stream_id}` | POST | 强制边界触发 |
| `/api/pipeline/set-detection-mode/{stream_id}` | PUT | 切换检测模式 |
| `/api/pipeline/export-metrics/{stream_id}` | GET | 导出检测指标 |
| `/api/knowledge-graph/nodes` | GET | 获取实体节点 |
| `/api/knowledge-graph/edges` | GET | 获取实体关系 |
| `/api/pipeline/stream` | WebSocket | 实时音频流 |
| `/api/pipeline/stream-test` | WebSocket | 测试模式流 |
