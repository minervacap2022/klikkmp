package com.klikcalendar.app.ui.components

/**
 * StatusOverlay - Hardware Integration Guide
 *
 * ⚠️ PLACEHOLDER DATA NOTICE ⚠️
 * Battery and Bluetooth indicators currently use PLACEHOLDER/MOCK data.
 * To integrate with real hardware:
 * 1. Remove placeholder battery/bluetooth data from SampleData.kt (search for "PLACEHOLDER_REMOVE")
 * 2. Implement platform-specific battery/BLE monitoring (see HARDWARE DATA FLOW below)
 * 3. Update KlikRepository to fetch real battery and BLE status
 * 4. Connect ViewModel to use real data instead of SampleData
 *
 * BATTERY STATUS:
 * - Hardware integration point: The Battery indicator shows device battery level from hardware.
 * - Value format: Percentage string (e.g., "85%") or descriptive state (e.g., "Charging", "Low")
 * - StatusLevel mapping:
 *   - Normal: Battery level > 20%
 *   - Warning: Battery level between 10-20%
 *   - Critical: Battery level < 10%
 * - Future: Consider adding battery health metrics from hardware sensors
 *
 * BLUETOOTH CONNECTIVITY:
 * - Hardware integration point: The BLE (Bluetooth Low Energy) indicator shows connection status.
 * - Value format: Connection state (e.g., "Connected", "Pairing", "Disconnected")
 * - StatusLevel mapping:
 *   - Normal: Connected and stable
 *   - Warning: Connection weak or intermittent
 *   - Critical: Disconnected or failed
 * - Future: Consider adding signal strength (RSSI) and connected device count
 *
 * HARDWARE DATA FLOW:
 * 1. Hardware sensors/BLE stack → Platform-specific native code
 * 2. Native code → KMP expect/actual interface
 * 3. KMP interface → KlikRepository → ViewModel
 * 4. ViewModel updates StatusOverlayState with current values
 * 5. StatusOverlay composable renders indicators based on state
 *
 * IMPLEMENTATION NOTES:
 * - Battery data: Use platform APIs (Android BatteryManager, iOS UIDevice.current.batteryLevel)
 * - BLE data: Use platform BLE APIs (Android BluetoothAdapter, iOS CoreBluetooth)
 * - Polling frequency: 30-60 seconds for battery, real-time for BLE connection changes
 * - Consider battery optimization: don't poll too frequently on mobile devices
 */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.klikcalendar.app.model.DeviceIndicator
import com.klikcalendar.app.model.DeviceIndicatorType
import com.klikcalendar.app.model.StatusLevel
import com.klikcalendar.app.model.StatusOverlayState
import com.klikcalendar.app.strings.AppStrings
import kotlinx.datetime.LocalDateTime

private fun LocalDateTime.toClock(): String =
    "${hour.pad2()}:${minute.pad2()}"

@Composable
fun StatusOverlay(
    state: StatusOverlayState,
    strings: AppStrings,
    onProfile: () -> Unit,
    onLanguageToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Status indicators in a compact row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Hide the two indicators to the right of Sync: Sensors and Network
                    state.indicators
                        .filter { it.type != DeviceIndicatorType.Sensors && it.type != DeviceIndicatorType.Network }
                        .forEach { indicator ->
                            IndicatorPillMinimal(indicator, strings)
                        }
                }

                // Profile button on the right
                AssistChip(
                    onClick = onProfile,
                    label = { Text("我的") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(),
                )
            }

            // Show language toggle if needed (can be expanded)
            AnimatedVisibility(
                visible = false, // Hidden by default for minimal design
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = onLanguageToggle,
                        label = { Text(strings.languageToggle) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Language,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun IndicatorPillMinimal(indicator: DeviceIndicator, strings: AppStrings) {
    // Defensive: completely suppress Sensors and Network regardless of upstream filters
    if (indicator.type == DeviceIndicatorType.Sensors || indicator.type == DeviceIndicatorType.Network) {
        return
    }
    val (icon, tint) = when (indicator.type) {
        DeviceIndicatorType.Battery -> Icons.Outlined.BatteryChargingFull to levelColor(indicator.level)
        DeviceIndicatorType.Ble -> Icons.Outlined.Bluetooth to levelColor(indicator.level)
        DeviceIndicatorType.Sync -> Icons.Outlined.Sync to levelColor(indicator.level)
        DeviceIndicatorType.Recording -> Icons.Outlined.Aod to levelColor(indicator.level)
        DeviceIndicatorType.Network -> Icons.Outlined.SignalCellularAlt to levelColor(indicator.level)
        DeviceIndicatorType.Sensors -> Icons.Outlined.Lightbulb to levelColor(indicator.level)
    }

    Surface(
        modifier = Modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clickable(
                    enabled = indicator.actionable,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = labelForIndicator(indicator.type, strings),
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            // Actionable badge removed - previously showed a lightbulb icon
        }
    }
}

private fun labelForIndicator(type: DeviceIndicatorType, strings: AppStrings): String = when (type) {
    DeviceIndicatorType.Battery -> strings.battery
    DeviceIndicatorType.Ble -> strings.bluetooth
    DeviceIndicatorType.Recording -> strings.recording
    DeviceIndicatorType.Sync -> strings.sync
    DeviceIndicatorType.Sensors -> strings.sensors
    DeviceIndicatorType.Network -> strings.network
}

@Composable
private fun levelColor(level: StatusLevel): Color = when (level) {
    StatusLevel.Normal -> MaterialTheme.colorScheme.primary
    StatusLevel.Warning -> MaterialTheme.colorScheme.tertiary
    StatusLevel.Critical -> MaterialTheme.colorScheme.error
}

private fun Int.pad2(): String = toString().padStart(2, '0')
