# JSON 输出变量说明文档

---

## 1. 转录结果文件 (_results.json)

### 1.1 文件说明

**文件名格式**: `SESSION_{timestamp}_{hash}_results.json`
**生成阶段**: Stage 1（音频转录 + 说话人识别）
**用途**: 包含完整的转录文本、时间戳、说话人识别和置信度信息

### 1.2 数据结构

文件为 JSON 数组，每个元素代表一个转录片段：

```json
[
  {
    "timestamp": "0:00:00 - 0:00:05",
    "start_seconds": 0.0,
    "end_seconds": 5.32,
    "session_id": "SESSION_20251106_035410_2382a684",
    "voiceprint_id": "VP_49c4f59777d336c9",
    "confidence": 0.5586,
    "language": "en",
    "text": "Trip Crosby has joined the meeting.",
    "segment_id": "SESSION_20251106_035410_2382a684_seg_0000",
    "diarization_speaker": "SPEAKER_00"
  }
]
```

### 1.3 字段详解

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| `timestamp` | String | 人类可读的时间范围 | `"0:00:00 - 0:00:05"` |
| `start_seconds` | Float | 片段起始时间（秒） | `0.0` |
| `end_seconds` | Float | 片段结束时间（秒） | `5.32` |
| `session_id` | String | 会话唯一标识符 | `"SESSION_20251106_035410_2382a684"` |
| `voiceprint_id` | String | 说话人声纹 ID（跨会话唯一） | `"VP_49c4f59777d336c9"` |
| `confidence` | Float | 转录置信度（0-1），越高越准确 | `0.5586` |
| `language` | String | 检测到的语言代码（ISO 639-1） | `"en"`, `"zh"`, `"ja"` |
| `text` | String | 转录的文本内容 | `"Trip Crosby has joined the meeting."` |
| `segment_id` | String | 片段唯一标识符（用于溯源） | `"SESSION_..._seg_0000"` |
| `diarization_speaker` | String | 说话人标签（会话内临时标识） | `"SPEAKER_00"`, `"SPEAKER_01"` |

### 1.4 使用场景

- **展示转录字幕**: 根据 `timestamp` 和 `text` 显示带时间戳的字幕
- **说话人过滤**: 根据 `voiceprint_id` 筛选特定说话人的发言
- **音频同步**: 使用 `start_seconds` 和 `end_seconds` 与音频播放同步
- **质量评估**: 通过 `confidence` 值标记低质量片段（建议阈值 < 0.4）

---

## 2. 会议纪要文件 (_meetingminutes.json)

### 2.1 文件说明

**文件名格式**: `SESSION_{timestamp}_{hash}_meetingminutes.json`
**生成阶段**: Stage 5（会议纪要生成）
**用途**: 结构化的会议摘要，包含关键决策、行动项、风险和讨论要点

### 2.2 根级字段

```json
{
  "session_id": "SESSION_20251106_044317_2382a684",
  "metadata": {...},
  "meeting_minutes": {...}
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `session_id` | String | 会话 ID，关联其他输出文件 |
| `metadata` | Object | 会议元数据 |
| `meeting_minutes` | Object | 会议纪要主体内容 |

### 2.3 metadata 字段

```json
{
  "created_at": "2025-11-06T04:52:39.985645Z",
  "source_system": "cog-whisper-diarization",
  "pipeline_stage": 5,
  "pipeline_version": "1.0",
  "meeting_date": "2025-11-06T00:00:00",
  "meeting_duration": "0:03:58",
  "document_version": "1.0",
  "total_segments": 111
}
```

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| `created_at` | String (ISO 8601) | 文件生成时间 | `"2025-11-06T04:52:39.985645Z"` |
| `source_system` | String | 处理系统标识 | `"cog-whisper-diarization"` |
| `pipeline_stage` | Integer | 管道阶段编号 | `5` |
| `pipeline_version` | String | 管道版本 | `"1.0"` |
| `meeting_date` | String (ISO 8601) | 会议日期 | `"2025-11-06T00:00:00"` |
| `meeting_duration` | String | 会议时长（HH:MM:SS） | `"0:17:49"` |
| `document_version` | String | 文档版本 | `"1.0"` |
| `total_segments` | Integer | 总转录片段数 | `278` |

### 2.4 meeting_minutes.summary

```json
{
  "summary": {
    "brief": "会议聚焦于 Q2 战略优先级和人员配置..."
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `brief` | String | 会议简要摘要（Markdown 格式），包含关键主题、决策和行动项 |

### 2.5 meeting_minutes.key_decisions

关键决策列表：

```json
{
  "key_decisions": [
    {
      "decision_id": "DEC_001",
      "description": "批准了新的产品路线图，预计 Q3 发布 v2.0 版本",
      "referenced_entities": [
        {
          "entity_id": "PROJ_b2a3f7fae9be08f7",
          "type": "project",
          "canonical_name": "Product V2.0"
        }
      ]
    }
  ]
}
```

#### 字段详解

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `decision_id` | String | 决策唯一标识符 |
| `description` | String | 决策内容描述 |
| `referenced_entities` | Array | 引用的实体列表 |
| `referenced_entities[].entity_id` | String | 实体 ID（关联知识图谱） |
| `referenced_entities[].type` | String | 实体类型：`person`, `project`, `company` |
| `referenced_entities[].canonical_name` | String | 实体规范名称 |

### 2.6 meeting_minutes.action_items (这个待定)

行动项列表：

```json
{
  "action_items": [
    {
      "action_id": "ACT_001",
      "description": "完成 API 文档编写并发送给开发团队",
      "owner": {
        "voiceprint_id": "VP_49c4f59777d336c9",
        "name": "Beth"
      },
      "deadline": "2025-11-15",
      "status": "pending",
      "priority": "high",
      "dependencies": [],
      "links": [],
      "referenced_entities": []
    }
  ]
}
```

#### 字段详解

| 字段名 | 类型 | 说明 | 可选值 |
|--------|------|------|--------|
| `action_id` | String | 行动项唯一标识符 | - |
| `description` | String | 任务描述 | - |
| `owner` | Object | 负责人信息 | - |
| `owner.voiceprint_id` | String | 负责人声纹 ID | - |
| `owner.name` | String | 负责人姓名 | - |
| `deadline` | String | 截止日期（YYYY-MM-DD） | `"TBD"` 表示待定 |
| `status` | String | 任务状态 | `pending`, `in_progress`, `completed` |
| `priority` | String | 优先级 | `high`, `medium`, `low` |
| `dependencies` | Array | 依赖的其他任务 ID | - |
| `links` | Array | 相关链接 | - |
| `referenced_entities` | Array | 引用的实体 | 同 key_decisions |

### 2.7 meeting_minutes.risks_and_blockers

风险与阻碍列表：

```json
{
  "risks_and_blockers": [
    {
      "risk_id": "RSK_001",
      "description": "服务器容量可能不足以支撑 Q4 用户增长",
      "mitigation": "计划在 11 月前完成服务器扩容",
      "responsible": {
        "voiceprint_id": "VP_c3a533d37e5886ca",
        "name": "Tyler"
      },
      "severity": "high",
      "referenced_entities": []
    }
  ]
}
```

#### 字段详解

| 字段名 | 类型 | 说明 | 可选值 |
|--------|------|------|--------|
| `risk_id` | String | 风险唯一标识符 | - |
| `description` | String | 风险描述 | - |
| `mitigation` | String | 缓解策略 | - |
| `responsible` | Object | 负责人信息 | - |
| `severity` | String | 严重程度 | `high`, `medium`, `low` |
| `referenced_entities` | Array | 引用的实体 | - |

### 2.8 meeting_minutes.discussion_points

讨论要点列表：

```json
{
  "discussion_points": [
    {
      "topic": "Q3 产品发布计划",
      "key_points": [
        "讨论了新功能的优先级排序",
        "确认了测试时间表",
        "决定推迟非核心功能到 Q4"
      ],
      "referenced_entities": []
    }
  ]
}
```

#### 字段详解

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `topic` | String | 讨论主题 |
| `key_points` | Array\<String\> | 关键要点列表 |
| `referenced_entities` | Array | 引用的实体 |

### 2.9 meeting_minutes.participants

参会人员信息：

```json
{
  "participants": {
    "attendees": [
      {
        "voiceprint_id": "VP_49c4f59777d336c9",
        "name": "Beth",
        "role": "Product Manager",
        "organization_id": "COM_GitLab_001",
        "organization_name": "GitLab"
      }
    ],
    "absent": []
  }
}
```

#### 字段详解

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `attendees` | Array | 参会人员列表 |
| `attendees[].voiceprint_id` | String | 声纹 ID |
| `attendees[].name` | String | 姓名 |
| `attendees[].role` | String | 职位/角色 |
| `attendees[].organization_id` | String/null | 所属组织 ID |
| `attendees[].organization_name` | String | 所属组织名称 |
| `absent` | Array | 缺席人员列表（结构同上） |

---

## 3. 待办事项文件 (_todos.json)

### 3.1 文件说明

**文件名格式**: `SESSION_{timestamp}_{hash}_todos.json`
**生成阶段**: Stage 6（待办事项提取）
**用途**: 从会议中提取的可执行任务，分类为 A-E 五个等级

### 3.2 根级结构

```json
{
  "session_id": "SESSION_20251106_044317_2382a684",
  "generated_at": "2025-11-06T05:18:54.144851",
  "total_todos": 3,
  "todos": [...],
  "category_distribution": {...},
  "category_descriptions": {...}
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `session_id` | String | 会话 ID |
| `generated_at` | String (ISO 8601) | 生成时间 |
| `total_todos` | Integer | 总任务数 |
| `todos` | Array | 任务列表 |
| `category_distribution` | Object | 各类别任务数量统计 |
| `category_descriptions` | Object | 类别说明 |

### 3.3 todos 数组元素

```json
{
  "id": "TODO_001",
  "category": "C",
  "title": "发送邀请给某位参与者",
  "description": "向新成员发送会议邀请，确保他们能参加下次讨论",
  "priority": "high",
  "owner": {
    "voiceprint_id": "VP_c3a533d37e5886ca",
    "name": "Tyler"
  },
  "related_entities": {
    "people": ["VP_49c4f59777d336c9"],
    "organizations": [],
    "projects": []
  },
  "related_segment_ids": ["SESSION_20251106_044317_2382a684_seg_0027"],
  "deadline": "2025-11-10",
  "status": "pending",
  "complexity": "low",
  "source": "transcript"
}
```

#### 字段详解

| 字段名 | 类型 | 说明 | 可选值 |
|--------|------|------|--------|
| `id` | String | 任务唯一标识符 | - |
| `category` | String | 任务分类（见 3.4 节） | `A`, `B`, `C`, `D`, `E` |
| `title` | String | 任务标题（简短） | - |
| `description` | String | 任务详细描述 | - |
| `priority` | String | 优先级 | `high`, `medium`, `low` |
| `owner` | Object | 负责人信息 | - |
| `related_entities` | Object | 关联实体 | - |
| `related_entities.people` | Array | 关联人员的声纹 ID 列表 | - |
| `related_entities.organizations` | Array | 关联组织 ID 列表 | - |
| `related_entities.projects` | Array | 关联项目 ID 列表 | - |
| `related_segment_ids` | Array | 关联的转录片段 ID（溯源用） | - |
| `deadline` | String | 截止日期 | `""` 表示无截止日期 |
| `status` | String | 任务状态 | `pending`, `in_progress`, `completed` |
| `complexity` | String | 任务复杂度 | `high`, `medium`, `low` |
| `source` | String | 任务来源 | `transcript`, `manual` |

### 3.4 任务分类说明

```json
{
  "category_descriptions": {
    "A": "需要外部 API（查询股价、天气、汇率等）",
    "B": "应用内部操作（添加日历、保存笔记）",
    "C": "需要用户确认（发送邮件、购买、支付）",
    "D": "复杂工作流（生成报告、数据分析）",
    "E": "简单提醒（喝水、休息、待办事项）"
  }
}
```

**移动端建议处理**:
- **A 类**: 调用第三方 API 获取数据
- **B 类**: 直接在应用内执行（如添加日历事件）
- **C 类**: 弹出确认对话框
- **D 类**: 提交到后台处理队列
- **E 类**: 设置系统通知/提醒

### 3.5 category_distribution

```json
{
  "category_distribution": {
    "A": 0,
    "B": 0,
    "C": 2,
    "D": 1,
    "E": 0
  }
}
```

各类别任务数量统计，用于 UI 数据可视化。

---

## 4. 优化转录文件 (_optimizedtranscript.json)

### 4.1 文件说明

**文件名格式**: `SESSION_{timestamp}_{hash}_optimizedtranscript.json`
**生成阶段**: Stage 4（实体提取与优化）
**用途**: 增强版转录，包含实体标注（人物、公司、项目）和关系信息

### 4.2 根级结构

```json
{
  "session_id": "SESSION_20251106_035410_2382a684",
  "metadata": {...},
  "segments": [...]
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `session_id` | String | 会话 ID |
| `metadata` | Object | 元数据 |
| `segments` | Array | 优化后的转录片段 |

### 4.3 metadata 字段

```json
{
  "created_at": "2025-11-06T04:01:53.419726Z",
  "source_system": "cog-whisper-diarization",
  "pipeline_stage": 4,
  "pipeline_version": "1.2-batched-qwen",
  "total_segments": 111,
  "batch_size": 20,
  "entity_context": {
    "people_count": 4,
    "companies_count": 1,
    "projects_count": 1,
    "relationships_count": 1
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `pipeline_version` | String | 管道版本（含处理器类型） |
| `batch_size` | Integer | 批处理大小（影响处理速度） |
| `entity_context` | Object | 实体统计信息 |
| `entity_context.people_count` | Integer | 识别的人物数量 |
| `entity_context.companies_count` | Integer | 识别的公司数量 |
| `entity_context.projects_count` | Integer | 识别的项目数量 |
| `entity_context.relationships_count` | Integer | 识别的关系数量 |

### 4.4 segments 数组元素

```json
{
  "segment_id": "SESSION_20251106_035410_2382a684_seg_0000",
  "timestamp": "0:00:00 - 0:00:05",
  "speaker": {
    "voiceprint_id": "VP_49c4f59777d336c9",
    "name": "Beth",
    "role_titles": ["Product Manager"],
    "works_for_company_id": "COM_GitLab_001",
    "seniority": "Senior",
    "skills": ["Product Strategy", "User Research"],
    "voiceprint_confidence": 0.85
  },
  "text": "Trip Crosby has joined the meeting.",
  "original_confidence": 0.5586,
  "entities": [
    {
      "text": "Trip Crosby",
      "span": [0, 11],
      "type": "person",
      "entity_id": "VP_PARTICIPANT_4e209042785ecc0f",
      "canonical_name": "Trip Crosby",
      "confidence": 0.85
    }
  ],
  "relationships": [],
  "metadata": {
    "language": "en",
    "words": [],
    "avg_logprob": 0.0
  }
}
```

#### 4.4.1 speaker 字段

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `voiceprint_id` | String | 说话人声纹 ID |
| `name` | String | 说话人姓名 |
| `role_titles` | Array\<String\> | 职位头衔列表 |
| `works_for_company_id` | String/null | 所属公司 ID |
| `seniority` | String | 资历级别（Junior, Mid, Senior, Lead） |
| `skills` | Array\<String\> | 技能标签 |
| `voiceprint_confidence` | Float | 声纹匹配置信度（0-1） |

#### 4.4.2 entities 数组

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `text` | String | 实体在文本中的原始字符串 |
| `span` | Array\<Integer\> | 实体在 text 中的起止位置 [start, end] |
| `type` | String | 实体类型：`person`, `company`, `project`, `location`, `date` |
| `entity_id` | String | 实体唯一标识符（关联知识图谱） |
| `canonical_name` | String | 实体规范名称（消歧后） |
| `confidence` | Float | 实体识别置信度（0-1） |

#### 4.4.3 relationships 数组

```json
{
  "relationships": [
    {
      "source_entity_id": "VP_49c4f59777d336c9",
      "target_entity_id": "PROJ_b2a3f7fae9be08f7",
      "relationship_type": "leads",
      "confidence": 0.78,
      "evidence": "Beth mentioned she is leading the Free User Initiative"
    }
  ]
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `source_entity_id` | String | 源实体 ID |
| `target_entity_id` | String | 目标实体 ID |
| `relationship_type` | String | 关系类型（见下文） |
| `confidence` | Float | 关系置信度（0-1） |
| `evidence` | String | 证据文本（从哪里推断出关系） |

**常见关系类型**:
- `works_for`: 人物 → 公司（雇佣关系）
- `leads`: 人物 → 项目（领导关系）
- `assigned_to`: 人物 → 项目（分配关系）
- `project_belongs_to`: 项目 → 公司（归属关系）

### 4.5 使用场景

- **实体高亮**: 根据 `entities[].span` 在文本中高亮显示实体
- **知识图谱可视化**: 使用 `relationships` 数据绘制关系图
- **智能搜索**: 根据实体类型（`entities[].type`）进行分类搜索
- **人物详情页**: 通过 `speaker` 字段展示人物画像

---

## 5. 运行历史文件 (run_history/{run_id}.json)

### 5.1 文件说明

**文件路径**: `/root/KK_pipelinetests/run_history/{run_id}.json`
**生成时机**: 管道执行完成或失败时
**用途**: 持久化存储完整的执行历史，包括日志、状态、结果文件路径

### 5.2 数据结构

```json
{
  "runId": "04523c53-1e83-4bb6-a274-17667d3f49bf",
  "sessionId": "session_1762425403061_ra2duqssw",
  "status": "COMPLETED",
  "startTime": "2025-11-06T10:36:54.815268",
  "endTime": "2025-11-06T10:41:07.328646",
  "executionTime": 252.513,
  "inputFile": "/tmp/klik_uploads/04523c53_audio.mp3",
  "logs": [
    "[10:36:54] Starting pipeline execution...",
    "[10:37:02] ============================================================",
    "[10:37:02] STAGE 1: Audio Transcription + Speaker Diarization",
    "[10:38:27] ✓ Stage 1 completed in 84.9s"
  ],
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
    "stage1_output": "/root/.../SESSION_xxx_results.json",
    "stage5_output": "/root/.../SESSION_xxx_meetingminutes.json",
    "stage6_output": "/root/.../SESSION_xxx_todos.json"
  },
  "frontendData": {
    "session_id": "SESSION_20251106_035410_2382a684",
    "participants": ["Beth", "Tyler"],
    "duration": "17:49",
    "language": "en"
  }
}
```

### 5.3 字段详解

| 字段名 | 类型 | 说明 | 可选值 |
|--------|------|------|--------|
| `runId` | String | 运行唯一标识符 | - |
| `sessionId` | String | 会话 ID（用于关联输出文件） | - |
| `status` | String | 执行状态 | `RUNNING`, `COMPLETED`, `FAILED` |
| `startTime` | String (ISO 8601) | 开始时间 | - |
| `endTime` | String (ISO 8601) | 结束时间 | - |
| `executionTime` | Float/null | 执行耗时（秒） | - |
| `inputFile` | String | 输入音频文件路径 | - |
| `logs` | Array\<String\> | 完整日志消息列表 | - |
| `modules` | Array\<String\> | 已完成的阶段列表 | - |
| `dropboxLinks` | Array\<String\> | Dropbox 下载链接（如已配置） | - |
| `databaseStats` | Object | 数据库统计信息 | - |
| `error` | String/null | 错误信息（失败时） | - |
| `completeResult` | Object/null | 完整结果对象 | - |
| `frontendData` | Object/null | 前端展示数据 | - |

### 5.4 completeResult 结构

```json
{
  "completeResult": {
    "stage1_output": "/root/.../SESSION_xxx_results.json",
    "stage2_speaker_names": "/root/.../voiceprint_database.json",
    "stage3_worklife": "/root/.../worklife_database.json",
    "stage4_optimized": "/root/.../SESSION_xxx_optimizedtranscript.json",
    "stage5_minutes": "/root/.../SESSION_xxx_meetingminutes.json",
    "stage6_todos": "/root/.../SESSION_xxx_todos.json",
    "denoised_audio": "/root/.../SESSION_xxx_denoised.wav",
    "vad_filtered_audio": "/root/.../SESSION_xxx_vad_filtered.wav"
  }
}
```

**用途**: 通过这些路径读取各阶段的输出文件。

---

## 6. 声纹数据库 (voiceprint_database.json)

### 6.1 文件说明

**文件路径**: `/root/KK_pipelinetests/test_results_stage1/voiceprint_database.json`
**更新时机**: 每次处理音频后更新
**用途**: 存储所有说话人的声纹特征向量，实现跨会话的说话人识别

### 6.2 数据结构

```json
{
  "voiceprints": [
    {
      "voiceprint_id": "VP_49c4f59777d336c9",
      "name": "Beth",
      "embeddings": [
        {
          "session_id": "SESSION_20251106_035410_2382a684",
          "embedding": [0.123, -0.456, 0.789, ...],
          "duration": 125.6,
          "sample_count": 15
        }
      ],
      "total_samples": 3,
      "first_seen": "2025-11-01T10:00:00Z",
      "last_updated": "2025-11-06T04:52:39Z",
      "metadata": {
        "age_group": "adult",
        "gender": "female",
        "accent": "US English"
      }
    }
  ],
  "total_voiceprints": 65,
  "database_version": "1.0"
}
```

### 6.3 字段详解

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `voiceprint_id` | String | 声纹唯一标识符 |
| `name` | String | 说话人姓名（从会议中提取或手动标注） |
| `embeddings` | Array | 声纹特征向量列表 |
| `embeddings[].session_id` | String | 采集会话 ID |
| `embeddings[].embedding` | Array\<Float\> | 192 维特征向量 |
| `embeddings[].duration` | Float | 采样总时长（秒） |
| `embeddings[].sample_count` | Integer | 合并的样本数 |
| `total_samples` | Integer | 总样本数（跨会话） |
| `first_seen` | String (ISO 8601) | 首次出现时间 |
| `last_updated` | String (ISO 8601) | 最后更新时间 |
| `metadata` | Object | 说话人元数据（可选） |

**注意**: `embedding` 数组通常包含 192 个浮点数，用于声纹匹配算法。

---

## 7. 会话数据库 (session_database.json)

### 7.1 文件说明

**文件路径**: `/root/KK_pipelinetests/test_results_stage1/session_database.json`
**用途**: 记录所有会话的元数据和参与者信息

### 7.2 数据结构

```json
{
  "sessions": [
    {
      "session_id": "SESSION_20251106_035410_2382a684",
      "created_at": "2025-11-06T03:54:10Z",
      "audio_file": "/root/.../audio.mp3",
      "duration": 1069.0,
      "language": "en",
      "participants": [
        {
          "voiceprint_id": "VP_49c4f59777d336c9",
          "name": "Beth",
          "speaking_time": 450.2,
          "utterances": 45
        },
        {
          "voiceprint_id": "VP_PARTICIPANT_4e209042785ecc0f",
          "name": "Trip Crosby",
          "speaking_time": 0.0,
          "utterances": 0,
          "note": "mentioned but did not speak"
        }
      ],
      "total_segments": 111,
      "processing_status": "completed"
    }
  ],
  "total_sessions": 14,
  "database_version": "1.0"
}
```

### 7.3 字段详解

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `session_id` | String | 会话唯一标识符 |
| `created_at` | String (ISO 8601) | 会话创建时间 |
| `audio_file` | String | 音频文件路径 |
| `duration` | Float | 会话时长（秒） |
| `language` | String | 检测到的主要语言 |
| `participants` | Array | 参与者列表 |
| `participants[].voiceprint_id` | String | 声纹 ID |
| `participants[].name` | String | 姓名 |
| `participants[].speaking_time` | Float | 发言时长（秒） |
| `participants[].utterances` | Integer | 发言次数 |
| `participants[].note` | String | 备注（如"提及但未发言"） |
| `total_segments` | Integer | 总转录片段数 |
| `processing_status` | String | 处理状态（completed/failed） |

---

## 8. 知识图谱数据库 (worklife_database.json)

### 8.1 文件说明

**文件路径**: `/root/KK_pipelinetests/test_results_stage1/worklife_database.json`
**生成阶段**: Stage 3（知识图谱提取）
**用途**: 存储从会议中提取的工作关系、组织架构和项目信息

### 8.2 数据结构（简化）

```json
{
  "people": [
    {
      "person_id": "VP_49c4f59777d336c9",
      "name": "Beth",
      "role_titles": ["Product Manager"],
      "seniority": "Senior",
      "skills": ["Product Strategy", "Stakeholder Management"],
      "works_for": "COM_GitLab_001",
      "confidence": 0.85,
      "evidence_segments": ["SESSION_xxx_seg_0012"]
    }
  ],
  "companies": [
    {
      "company_id": "COM_GitLab_001",
      "name": "GitLab",
      "confidence": 0.92,
      "evidence_segments": ["SESSION_xxx_seg_0003", "SESSION_xxx_seg_0008"]
    }
  ],
  "projects": [
    {
      "project_id": "PROJ_b2a3f7fae9be08f7",
      "name": "Free User Initiative",
      "status": "active",
      "phase": "Operate",
      "leader": "VP_PARTICIPANT_c88fceca29b2c2fd",
      "confidence": 0.78,
      "evidence_segments": ["SESSION_xxx_seg_0025"]
    }
  ],
  "relationships": [
    {
      "source_id": "VP_49c4f59777d336c9",
      "target_id": "PROJ_b2a3f7fae9be08f7",
      "relationship_type": "leads",
      "confidence": 0.80,
      "evidence": "Beth mentioned leading the Free User Initiative"
    }
  ]
}
```

### 8.3 字段详解

#### 8.3.1 people 数组

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `person_id` | String | 人物 ID（通常是声纹 ID） |
| `name` | String | 姓名 |
| `role_titles` | Array\<String\> | 职位头衔 |
| `seniority` | String | 资历级别 |
| `skills` | Array\<String\> | 技能列表 |
| `works_for` | String | 所属公司 ID |
| `confidence` | Float | 信息置信度（0-1） |
| `evidence_segments` | Array\<String\> | 证据片段 ID |

#### 8.3.2 companies 数组

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `company_id` | String | 公司唯一标识符 |
| `name` | String | 公司名称 |
| `confidence` | Float | 识别置信度 |
| `evidence_segments` | Array\<String\> | 证据片段 ID |

#### 8.3.3 projects 数组

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `project_id` | String | 项目唯一标识符 |
| `name` | String | 项目名称 |
| `status` | String | 项目状态（active/completed/planned） |
| `phase` | String | 项目阶段（Plan/Build/Operate） |
| `leader` | String | 项目负责人 ID |
| `confidence` | Float | 识别置信度 |
| `evidence_segments` | Array\<String\> | 证据片段 ID |

#### 8.3.4 relationships 数组

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `source_id` | String | 源实体 ID |
| `target_id` | String | 目标实体 ID |
| `relationship_type` | String | 关系类型（works_for, leads, assigned_to） |
| `confidence` | Float | 关系置信度 |
| `evidence` | String | 证据文本 |

---
## 11. 字段索引

### 11.1 常用 ID 字段

| 字段名 | 格式 | 示例 | 用途 |
|--------|------|------|------|
| `session_id` | `SESSION_{timestamp}_{hash}` | `SESSION_20251106_035410_2382a684` | 会话标识 |
| `voiceprint_id` | `VP_{hash}` | `VP_49c4f59777d336c9` | 说话人标识 |
| `run_id` | UUID | `04523c53-1e83-4bb6-a274-17667d3f49bf` | 运行标识 |
| `segment_id` | `{session_id}_seg_{number}` | `SESSION_xxx_seg_0000` | 片段标识 |
| `company_id` | `COM_{name}_{hash}` | `COM_GitLab_001` | 公司标识 |
| `project_id` | `PROJ_{hash}` | `PROJ_b2a3f7fae9be08f7` | 项目标识 |

### 11.2 时间相关字段

| 字段名 | 格式 | 示例 |
|--------|------|------|
| `timestamp` | `HH:MM:SS - HH:MM:SS` | `"0:00:00 - 0:00:05"` |
| `start_seconds` | Float | `0.0` |
| `end_seconds` | Float | `5.32` |
| `created_at` | ISO 8601 | `"2025-11-06T04:52:39.985645Z"` |
| `meeting_duration` | `HH:MM:SS` | `"0:17:49"` |
| `deadline` | YYYY-MM-DD | `"2025-11-15"` |

### 11.3 置信度字段

| 字段名 | 范围 | 质量标准 |
|--------|------|----------|
| `confidence` | 0.0 - 1.0 | > 0.7 高质量，< 0.4 低质量 |
| `voiceprint_confidence` | 0.0 - 1.0 | > 0.8 可信，< 0.5 需人工确认 |
| `entity.confidence` | 0.0 - 1.0 | > 0.75 可直接使用 |


---

**文档完成！** 如有疑问，请参考 [API 端点文档](./API端点文档_移动端调用指南.md) 或联系技术支持。
