# Klik 日历应用 - Kotlin Multiplatform

一个基于 Kotlin Multiplatform 构建的现代化日历与工作流管理应用。

## 📱 项目概述

Klik 是一个集成了日历、会议转录、任务管理和知识图谱的综合性工作管理应用。采用 Kotlin Multiplatform (KMP) 技术栈，支持 Android 和 iOS 平台。

## ✨ 核心功能

### 📅 日程管理
- 时间线视图展示每日事件
- 事件状态跟踪（待定、已确认、已拒绝、已归档）
- 事件阶段管理（发现、参与、协商、完成）
- 按状态和阶段筛选事件

### 🎙️ 硬件集成
- **电池状态监控**：实时显示设备电量（目前使用占位数据）
- **蓝牙连接状态**：显示 BLE 连接状态（目前使用占位数据）
- **同步状态**：显示数据同步状态和待处理项

### 📝 反馈系统
- 卡片式反馈界面
- 滑动交互：左滑表示错误，右滑表示正确，上滑表示不确定
- 支持多种反馈类型：
  - 说话人识别
  - 任务提取
  - 关系抽取
  - 会议结果
- 圆形缩放动画效果

### 🌐 知识图谱
- 可视化展示人物、团队、项目和资产之间的关系
- 实时指标监控
- 关联节点浏览

### ✅ 任务管理
- 运营任务跟踪
- 自动化识别标记
- 任务分类和优先级管理

## 🏗️ 技术架构

### 技术栈
- **UI 框架**：Jetpack Compose Multiplatform
- **语言**：Kotlin
- **架构模式**：MVVM + Repository Pattern
- **并发**：Kotlin Coroutines + Flow
- **日期时间**：kotlinx-datetime

### 项目结构
```
Klik_UI/
├── app/                    # Android 应用模块
├── shared/                 # 共享代码模块
│   ├── src/main/kotlin/
│   │   └── com/klikcalendar/app/
│   │       ├── data/       # 数据层（Repository）
│   │       ├── model/      # 数据模型
│   │       ├── state/      # 应用状态管理
│   │       ├── strings/    # 多语言字符串
│   │       └── ui/         # UI 组件和屏幕
│   │           ├── components/  # 可复用 UI 组件
│   │           ├── screens/     # 完整屏幕
│   │           └── theme/       # 主题配置
├── gradle/                 # Gradle 配置
└── build.gradle.kts       # 项目构建配置
```

## 🚀 快速开始

### 环境要求
- JDK 17 或更高版本
- Android Studio Hedgehog (2023.1.1) 或更新版本
- Gradle 8.0+

### 构建项目

1. **克隆仓库**
```bash
git clone https://github.com/minervacap2022/klikkmp.git
cd klikkmp
```

2. **构建项目**
```bash
./gradlew build
```

3. **运行 Android 应用**
```bash
./gradlew :app:installDebug
```

或在 Android Studio 中直接运行。

## ⚠️ 占位数据说明

**当前版本使用占位数据用于 UI 开发和测试。**

以下功能使用占位数据（在代码中标记为 `⚠️ PLACEHOLDER_REMOVE`）：
- ✅ 电池状态（显示 85%）
- ✅ 蓝牙连接状态（显示"在线"）
- ✅ 反馈卡片（10 张测试卡片）
- ✅ 日程事件
- ✅ 会议转录记录
- ✅ 知识图谱节点

### 移除占位数据

当准备集成真实后端时，在代码中搜索 `PLACEHOLDER_REMOVE` 标记：

```bash
grep -r "PLACEHOLDER_REMOVE" .
```

然后按照注释中的说明替换为真实的数据源。

## 🌍 多语言支持

应用支持简体中文和英文，可在界面中切换：
- 中文（默认）
- English

## 📂 主要文件说明

- `CLAUDE.md` - 项目变更日志（自动更新）
- `BUILD_INSTRUCTIONS.md` - 详细构建说明
- `BACKEND_INTEGRATION_GUIDE.md` - 后端集成指南
- `QUICK_START.md` - 快速开始指南
- `Designs.md` - 设计文档

## 🎨 UI 特性

- **Material Design 3** 设计系统
- **响应式布局** 适配不同屏幕尺寸
- **流畅动画** 包括圆形缩放、淡入淡出等
- **深色模式支持**（待实现）

## 🔧 硬件集成准备

应用已为硬件集成做好准备：

### 电池状态集成
- 使用平台 API：
  - Android: `BatteryManager`
  - iOS: `UIDevice.current.batteryLevel`
- 建议轮询频率：30-60 秒
- 状态等级映射：
  - 正常：电量 > 20%
  - 警告：电量 10-20%
  - 严重：电量 < 10%

### 蓝牙集成
- 使用平台 API：
  - Android: `BluetoothAdapter`
  - iOS: `CoreBluetooth`
- 实时连接状态监控
- 支持 BLE (Bluetooth Low Energy)

详细集成说明请参考 `StatusOverlay.kt` 文件顶部的文档注释。

## 📝 待办事项

- [ ] 集成真实后端 API
- [ ] 实现用户认证
- [ ] 添加深色模式支持
- [ ] 实现 iOS 平台支持
- [ ] 添加单元测试和 UI 测试
- [ ] 实现离线数据缓存
- [ ] 添加推送通知
- [ ] 实现硬件集成（电池、蓝牙）

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。

## 📧 联系方式

如有问题或建议，请通过 GitHub Issues 联系我们。

---

**注意**：本项目正在积极开发中，某些功能可能尚未完全实现或使用占位数据。
