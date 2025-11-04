package com.klikcalendar.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DeviceHub
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Upgrade
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.klikcalendar.shared.strings.AppStrings

@Composable
fun ProfileScreen(
    strings: AppStrings,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "返回",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    text = "我的",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        item {
            // AE1 - 设备信息
            // TODO: Load device info from backend API (GET /api/v1/device/info)
            ProfileSection(title = "设备信息") {
                InfoRow(label = "固件号", value = "--")
                HorizontalDivider()
                InfoRow(label = "设备名", value = "--")
                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "统计信息",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))

                InfoRow(label = "录音次数", value = "--")
                InfoRow(label = "录音时长", value = "--")
                InfoRow(label = "任务数量", value = "--")

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                InfoRow(
                    icon = Icons.Outlined.Storage,
                    label = "存储空间",
                    value = "--",
                )
            }
        }

        item {
            // 设备管理
            ProfileSection(title = "设备管理") {
                MenuItemRow(
                    icon = Icons.Outlined.Cable,
                    label = "连接新设备",
                    onClick = { /* TODO */ },
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.DeviceHub,
                    label = "断开连接",
                    onClick = { /* TODO */ },
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Refresh,
                    label = "重置设置",
                    onClick = { /* TODO */ },
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.MiscellaneousServices,
                    label = "重置一切",
                    onClick = { /* TODO */ },
                    isDestructive = true,
                )
            }
        }

        item {
            // 付费方案
            // TODO: Load subscription info from backend API (GET /api/v1/subscription)
            ProfileSection(title = "当前付费方案") {
                InfoRow(
                    icon = Icons.Outlined.Upgrade,
                    label = "方案",
                    value = "--",
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Upgrade,
                    label = "升级方案",
                    onClick = { /* TODO: Navigate to subscription upgrade */ },
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.CloudUpload,
                    label = "升级存储",
                    onClick = { /* TODO: Navigate to storage upgrade */ },
                )
            }
        }

        item {
            // AEA - Miniapp
            ProfileSection(title = "Miniapp") {
                MenuItemRow(
                    icon = Icons.Outlined.Extension,
                    label = "高频使用的 Miniapp",
                    onClick = { /* TODO: Navigate to high-frequency miniapps */ },
                    showArrow = true,
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Code,
                    label = "低频使用的 Miniapp",
                    onClick = { /* TODO: Navigate to low-frequency miniapps */ },
                    showArrow = true,
                )
            }
        }

        item {
            // AEA2 - 外部链接
            ProfileSection(title = "外部链接") {
                MenuItemRow(
                    icon = Icons.Outlined.Link,
                    label = "Notion",
                    onClick = { /* TODO: Open Notion */ },
                    showArrow = true,
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    onClick = { /* TODO: Open Email */ },
                    showArrow = true,
                )
            }
        }

        item {
            // AEB - 帮助与支持
            ProfileSection(title = "帮助与支持") {
                MenuItemRow(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    label = "指南",
                    onClick = { /* TODO */ },
                    showArrow = true,
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Email,
                    label = "客服与投诉",
                    onClick = { /* TODO */ },
                    showArrow = true,
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Info,
                    label = "关于",
                    onClick = { /* TODO */ },
                    showArrow = true,
                )
                HorizontalDivider()
                MenuItemRow(
                    icon = Icons.Outlined.Info,
                    label = "隐私政策",
                    onClick = { /* TODO */ },
                    showArrow = true,
                )
            }
        }

        item {
            // 登出按钮
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { /* TODO: Logout */ },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                    Text(
                        text = "登出",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun MenuItemRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    showArrow: Boolean = false,
    isDestructive: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isDestructive) MaterialTheme.colorScheme.error
                       else MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDestructive) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface,
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
