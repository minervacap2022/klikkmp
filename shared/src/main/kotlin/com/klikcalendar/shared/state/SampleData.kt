package com.klikcalendar.shared.state

/**
 * ⚠️ DEVELOPMENT ONLY - DO NOT USE IN PRODUCTION ⚠️
 *
 * This file contains sample data for development and testing purposes ONLY.
 * All production data should come from the backend API via KlikRepository.
 *
 * This data is currently NOT used in the app - the app uses empty data from
 * KlikRepositoryImpl until backend integration is complete.
 *
 * Usage:
 * - Use this data for UI previews and development testing
 * - DO NOT reference this object in production code
 * - DO NOT commit code that uses SampleData in the app initialization
 *
 * @see com.klikcalendar.shared.data.KlikRepository for production data access
 * @see com.klikcalendar.shared.data.KlikRepositoryImpl for backend integration
 */

import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.DeviceIndicator
import com.klikcalendar.shared.model.DeviceIndicatorType
import com.klikcalendar.shared.model.EventStage
import com.klikcalendar.shared.model.EventStatus
import com.klikcalendar.shared.model.MeetingMemo
import com.klikcalendar.shared.model.MetricSnapshot
import com.klikcalendar.shared.model.OperationTask
import com.klikcalendar.shared.model.QuickAction
import com.klikcalendar.shared.model.QuickActionType
import com.klikcalendar.shared.model.StatusLevel
import com.klikcalendar.shared.model.StatusOverlayState
import com.klikcalendar.shared.model.TimelineDay
import com.klikcalendar.shared.model.TranscriptChannel
import com.klikcalendar.shared.model.TranscriptRecord
import com.klikcalendar.shared.model.WorkLifeNode
import com.klikcalendar.shared.model.WorkLifeNodeType
import com.klikcalendar.shared.data.FeedbackItem
import com.klikcalendar.shared.data.FeedbackType
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

object SampleData {
    private val nowDate = LocalDate(2025, 10, 28)
    private val nextDay = nowDate.plus(DatePeriod(days = 1))
    private val previousDay = nowDate.minus(DatePeriod(days = 1))

    val overlay = StatusOverlayState(
        indicators = listOf(
            // ⚠️ PLACEHOLDER_REMOVE: Mock battery data - replace with real hardware integration
            DeviceIndicator(DeviceIndicatorType.Battery, "85%", StatusLevel.Normal, actionable = false),
            // ⚠️ PLACEHOLDER_REMOVE: Mock BLE data - replace with real hardware integration
            DeviceIndicator(DeviceIndicatorType.Ble, "Online", StatusLevel.Normal, actionable = true),
            DeviceIndicator(DeviceIndicatorType.Recording, "Active", StatusLevel.Normal, actionable = true),
            DeviceIndicator(DeviceIndicatorType.Sync, "2 pending", StatusLevel.Warning, actionable = true),
            DeviceIndicator(DeviceIndicatorType.Sensors, "5/6", StatusLevel.Warning),
            DeviceIndicator(DeviceIndicatorType.Network, "LTE", StatusLevel.Normal),
        ),
        miniAppCount = 4,
        syncTimestamp = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 9, 42, 0),
        alerts = listOf(
            "Sync backlog exceeds 2 min SLA",
            "Device A17 microphone drift detected",
        ),
    )

    val timeline = listOf(
        TimelineDay(
            date = nowDate,
            focus = true,
            utilization = 82,
            events = listOf(
                CalendarEvent(
                    id = "evt-1",
                    title = "Pipeline #9 Stand-up",
                    owner = "Iris",
                    start = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 9, 30),
                    end = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 10, 0),
                    stage = EventStage.Discovery,
                    status = EventStatus.Confirmed,
                    priority = 1,
                    location = "Atlas Room",
                    tags = listOf("Pipeline #9", "Sprint"),
                ),
                CalendarEvent(
                    id = "evt-2",
                    title = "Client Onboarding Review",
                    owner = "Chen",
                    start = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 11, 0),
                    end = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 12, 0),
                    stage = EventStage.Negotiation,
                    status = EventStatus.Pending,
                    priority = 2,
                    location = "Zoom",
                    tags = listOf("Finance", "Critical"),
                ),
                CalendarEvent(
                    id = "evt-3",
                    title = "Ops Playbook Drill",
                    owner = "Nikhil",
                    start = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 14, 30),
                    end = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 15, 15),
                    stage = EventStage.Engagement,
                    status = EventStatus.Pending,
                    priority = 3,
                    location = "Ops Lab",
                    tags = listOf("Ops", "Training"),
                ),
            ),
        ),
        TimelineDay(
            date = nextDay,
            focus = false,
            utilization = 65,
            events = listOf(
                CalendarEvent(
                    id = "evt-4",
                    title = "Mini App Launch Review",
                    owner = "Lina",
                    start = LocalDateTime(nextDay.year, nextDay.monthNumber, nextDay.dayOfMonth, 10, 0),
                    end = LocalDateTime(nextDay.year, nextDay.monthNumber, nextDay.dayOfMonth, 11, 30),
                    stage = EventStage.Closing,
                    status = EventStatus.Confirmed,
                    priority = 1,
                    location = "Studio",
                    tags = listOf("Mini App", "Product"),
                )
            ),
        ),
        TimelineDay(
            date = previousDay,
            focus = false,
            utilization = 74,
            events = listOf(
                CalendarEvent(
                    id = "evt-5",
                    title = "Voice Glue QA Sync",
                    owner = "Yuki",
                    start = LocalDateTime(previousDay.year, previousDay.monthNumber, previousDay.dayOfMonth, 16, 0),
                    end = LocalDateTime(previousDay.year, previousDay.monthNumber, previousDay.dayOfMonth, 16, 45),
                    stage = EventStage.Engagement,
                    status = EventStatus.Archived,
                    priority = 2,
                    location = "Lab 4B",
                    tags = listOf("QA", "Voice Glue"),
                )
            ),
        ),
    )

    val transcripts = listOf(
        TranscriptRecord(
            id = "rec-1",
            title = "Device sync incident triage",
            owner = "OpsBot",
            capturedAt = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 8, 25),
            channel = TranscriptChannel.Automation,
            summary = "Auto-escalation executed playbook OP-41 with 3 remediation steps.",
            status = EventStatus.Confirmed,
            attachments = listOf("ops/op-41.pdf"),
            requiresAction = false,
            relatedEventIds = setOf("evt-1"),
        ),
        TranscriptRecord(
            id = "rec-2",
            title = "Client onboarding call",
            owner = "Chen",
            capturedAt = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 11, 5),
            channel = TranscriptChannel.Call,
            summary = "Pending compliance sign-off. Schedule follow up within 24h.",
            status = EventStatus.Pending,
            attachments = emptyList(),
            requiresAction = true,
            relatedEventIds = setOf("evt-2"),
        ),
        TranscriptRecord(
            id = "rec-3",
            title = "Mini app beta feedback",
            owner = "Iris",
            capturedAt = LocalDateTime(previousDay.year, previousDay.monthNumber, previousDay.dayOfMonth, 13, 40),
            channel = TranscriptChannel.Meeting,
            summary = "Aggregated 12 feedback snippets; 3 flagged red.",
            status = EventStatus.Archived,
            attachments = listOf("feedback/beta.csv"),
            requiresAction = false,
            relatedEventIds = setOf("evt-3"),
        ),
    )

    val memos = listOf(
        MeetingMemo(
            id = "memo-1",
            meetingId = "evt-1",
            title = "Sprint Commitments",
            body = "• Confirmed focus on ingestion hotfix.\n• Align ops headcount for 11/02 launch.\n• Flagged need for BLE regression test coverage.",
            lastUpdated = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 10, 5),
            author = "Iris",
        ),
        MeetingMemo(
            id = "memo-2",
            meetingId = "evt-2",
            title = "Compliance Next Steps",
            body = "• Await finance KYC packet.\n• Draft onboarding sandbox walkthrough.\n• Schedule follow-up with legal by 17:00.",
            lastUpdated = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 12, 12),
            author = "Chen",
        ),
        MeetingMemo(
            id = "memo-3",
            meetingId = "evt-3",
            title = "Ops Lab Drill Notes",
            body = "• Identified automation gap on alert 403.\n• Need ops instrumentation for new telemetry endpoints.\n• Assign DRI for device loop test.",
            lastUpdated = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 15, 20),
            author = "Nikhil",
        ),
    )

    val knowledgeGraph = listOf(
        WorkLifeNode(
            id = "node-1",
            type = WorkLifeNodeType.Project,
            name = "Pipeline #9",
            description = "Voice Ops Expansion - Phase β",
            peers = 18,
            tags = listOf("AI Ops", "Voice"),
            metrics = listOf(
                MetricSnapshot("Velocity", "1.12x", "+0.08", StatusLevel.Normal),
                MetricSnapshot("Risk", "Medium", "-12%", StatusLevel.Warning),
            ),
            linkedNodes = listOf("node-2", "node-3"),
        ),
        WorkLifeNode(
            id = "node-2",
            type = WorkLifeNodeType.Team,
            name = "Ops Command",
            description = "Command center for operations escalations.",
            peers = 42,
            tags = listOf("Operations", "Escalation"),
            metrics = listOf(
                MetricSnapshot("SLA", "94%", "-3%", StatusLevel.Warning),
                MetricSnapshot("Alerts", "5 critical", "+2", StatusLevel.Critical),
            ),
            linkedNodes = listOf("node-1", "node-4"),
        ),
        WorkLifeNode(
            id = "node-3",
            type = WorkLifeNodeType.Person,
            name = "Chen Zhao",
            description = "Client success partner for beta programs.",
            peers = 128,
            tags = listOf("Client", "Success"),
            metrics = listOf(
                MetricSnapshot("Engagement", "87%", "+5%", StatusLevel.Normal),
                MetricSnapshot("Follow-ups", "3 pending", "+1", StatusLevel.Warning),
            ),
            linkedNodes = listOf("node-1"),
        ),
    )

    val operations = listOf(
        OperationTask(
            id = "ops-1",
            title = "Execute Device Reset Sweep",
            dueDate = nowDate,
            owner = "OpsBot",
            stage = "Automation",
            automationEligible = true,
            status = EventStatus.Pending,
            category = "Device",
            relatedEventIds = setOf("evt-1"),
        ),
        OperationTask(
            id = "ops-2",
            title = "Calibrate Meeting Capture",
            dueDate = nextDay,
            owner = "Field Team",
            stage = "Validation",
            automationEligible = false,
            status = EventStatus.Confirmed,
            category = "Quality",
            relatedEventIds = setOf("evt-2"),
        ),
        OperationTask(
            id = "ops-3",
            title = "Archive Legacy Session Logs",
            dueDate = previousDay,
            owner = "Ops Command",
            stage = "Archive",
            automationEligible = true,
            status = EventStatus.Archived,
            category = "Compliance",
            relatedEventIds = setOf("evt-3"),
        ),
    )

    val quickActions = listOf(
        QuickAction(
            id = "qa-1",
            type = QuickActionType.Meeting,
            label = "Log Meeting",
            description = "Create quick meeting capture in <30s.",
            relatedEventIds = setOf("evt-1"),
        ),
        QuickAction(
            id = "qa-2",
            type = QuickActionType.Device,
            label = "Register Device",
            description = "Pair a new Klik device via BLE.",
        ),
        QuickAction(
            id = "qa-3",
            type = QuickActionType.Notes,
            label = "Capture Notes",
            description = "Trigger voice-to-summary workflow.",
            relatedEventIds = setOf("evt-2"),
        ),
        QuickAction(
            id = "qa-4",
            type = QuickActionType.Automation,
            label = "Run Automation",
            description = "Execute scripted remediation.",
            relatedEventIds = setOf("evt-3"),
        ),
        QuickAction(
            id = "qa-5",
            type = QuickActionType.Task,
            label = "New Task",
            description = "Queue follow-up in Pipeline.",
        ),
    )

    // ⚠️ PLACEHOLDER_REMOVE: Mock feedback data for UI development
    // Remove these placeholder items when integrating with real backend feedback system
    // Search for "PLACEHOLDER_REMOVE" to find all related code
    val feedbackItems = listOf(
        FeedbackItem(
            id = "fb-1",
            title = "说话人识别",
            description = "请确认以下说话人是否正确",
            content = "SPEAKER_00 是张三吗？\n\n「张三，你对这个方案有什么看法？」",
            type = FeedbackType.SPEAKER_IDENTIFICATION,
        ),
        FeedbackItem(
            id = "fb-2",
            title = "任务提取",
            description = "请确认提取的任务是否正确",
            content = "原文：「那这样，李四你下周三之前把报告整理好，发给大家。」\n\n提取任务：李四 - 整理报告并发送 - 截止日期：下周三",
            type = FeedbackType.TASK_EXTRACTION,
        ),
        FeedbackItem(
            id = "fb-3",
            title = "关系抽取",
            description = "请确认提取的关系是否正确",
            content = "原文：「王五是我们项目的负责人，之前在阿里工作过。」\n\n提取关系：\n- 王五 [职位] 项目负责人\n- 王五 [工作经历] 阿里",
            type = FeedbackType.RELATIONSHIP_EXTRACTION,
        ),
        FeedbackItem(
            id = "fb-4",
            title = "会议结果",
            description = "请确认会议纪要是否准确",
            content = "会议主题：产品迭代讨论\n\n决议：\n1. 下周发布 2.0 版本\n2. 增加用户反馈功能\n3. 优化性能",
            type = FeedbackType.MEETING_RESULT,
        ),
        FeedbackItem(
            id = "fb-5",
            title = "说话人识别",
            description = "请确认以下说话人是否正确",
            content = "SPEAKER_02 是陈经理吗？\n\n「大家好，今天我们讨论一下项目进度。」",
            type = FeedbackType.SPEAKER_IDENTIFICATION,
        ),
        FeedbackItem(
            id = "fb-6",
            title = "任务提取",
            description = "请确认提取的任务是否正确",
            content = "原文：「赵六，你负责协调各部门，周五前完成需求对齐。」\n\n提取任务：赵六 - 协调部门需求对齐 - 截止日期：周五",
            type = FeedbackType.TASK_EXTRACTION,
        ),
        FeedbackItem(
            id = "fb-7",
            title = "关系抽取",
            description = "请确认提取的关系是否正确",
            content = "原文：「小李是我们的技术架构师，他在腾讯工作了五年。」\n\n提取关系：\n- 小李 [职位] 技术架构师\n- 小李 [工作经历] 腾讯 (5年)",
            type = FeedbackType.RELATIONSHIP_EXTRACTION,
        ),
        FeedbackItem(
            id = "fb-8",
            title = "会议结果",
            description = "请确认会议纪要是否准确",
            content = "会议主题：Q4 预算评审\n\n决议：\n1. 批准研发部门追加预算 15%\n2. 市场推广预算冻结至 12 月\n3. 下周一提交详细计划",
            type = FeedbackType.MEETING_RESULT,
        ),
        FeedbackItem(
            id = "fb-9",
            title = "说话人识别",
            description = "请确认以下说话人是否正确",
            content = "SPEAKER_01 是产品经理孙女士吗？\n\n「我们需要重新评估用户体验流程。」",
            type = FeedbackType.SPEAKER_IDENTIFICATION,
        ),
        FeedbackItem(
            id = "fb-10",
            title = "任务提取",
            description = "请确认提取的任务是否正确",
            content = "原文：「周总，麻烦您在月底前审批这份合同。」\n\n提取任务：周总 - 审批合同 - 截止日期：月底",
            type = FeedbackType.TASK_EXTRACTION,
        ),
    )
}
