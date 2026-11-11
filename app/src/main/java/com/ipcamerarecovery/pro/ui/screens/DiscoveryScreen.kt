package com.ipcamerarecovery.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.R
import com.ipcamerarecovery.pro.model.CameraDevice
import com.ipcamerarecovery.pro.model.Manufacturer
import com.ipcamerarecovery.pro.ui.components.VendorBadge
import com.ipcamerarecovery.pro.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    devices: List<CameraDevice>,
    isScanning: Boolean,
    onStartScan: () -> Unit,
    onDeviceSelected: (CameraDevice) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<Manufacturer?>(null) }

    val filteredDevices = remember(devices, searchQuery, selectedFilter) {
        devices.filter { device ->
            val matchesFilter = selectedFilter == null || device.manufacturer == selectedFilter
            val matchesSearch = searchQuery.isBlank() ||
                device.ipAddress.contains(searchQuery, ignoreCase = true) ||
                device.macAddress.contains(searchQuery, ignoreCase = true) ||
                device.model.contains(searchQuery, ignoreCase = true) ||
                device.serialNumber.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
    ) {
        // Top Action Bar & Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.discovery_title),
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.discovery_subtitle),
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onStartScan,
                enabled = !isScanning,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CctvPrimary,
                    disabledContainerColor = CctvPrimaryDark
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = if (isScanning) "Scanning..." else stringResource(R.string.btn_start_scan),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(stringResource(R.string.search_hint), fontSize = 13.sp, color = TextMuted)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = CctvPrimary,
                unfocusedBorderColor = CctvBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                containerColor = CctvSurface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Manufacturer Filter Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterTabChip(
                    label = stringResource(R.string.filter_all),
                    isSelected = selectedFilter == null,
                    onClick = { selectedFilter = null }
                )
            }
            item {
                FilterTabChip(
                    label = "Hikvision",
                    isSelected = selectedFilter == Manufacturer.HIKVISION,
                    onClick = { selectedFilter = Manufacturer.HIKVISION }
                )
            }
            item {
                FilterTabChip(
                    label = "Dahua",
                    isSelected = selectedFilter == Manufacturer.DAHUA,
                    onClick = { selectedFilter = Manufacturer.DAHUA }
                )
            }
            item {
                FilterTabChip(
                    label = "Uniview",
                    isSelected = selectedFilter == Manufacturer.UNIVIEW,
                    onClick = { selectedFilter = Manufacturer.UNIVIEW }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(stringResource(R.string.devices_found), filteredDevices.size),
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            if (isScanning) {
                Text(
                    text = "Probing UDP 37020 & 37810...",
                    color = CctvAccent,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Device List
        if (filteredDevices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Videocam,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isScanning) "Discovering IP cameras..." else "No devices found matching filter",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredDevices, key = { it.id }) { device ->
                    DeviceCardItem(device = device, onClick = { onDeviceSelected(device) })
                }
            }
        }
    }
}

@Composable
fun FilterTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CctvPrimary else CctvSurface)
            .border(1.dp, if (isSelected) CctvAccent else CctvBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun DeviceCardItem(
    device: CameraDevice,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CctvSurface)
            .border(1.dp, CctvBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VendorBadge(manufacturer = device.manufacturer)
                Text(
                    text = "${device.responseLatencyMs} ms",
                    color = StatusSuccess,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = device.model,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "IP Address", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = device.ipAddress,
                        color = CctvAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "MAC Address", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = device.macAddress,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SN: ${device.serialNumber.take(16)}...",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "FW: ${device.firmwareVersion.take(14)}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
