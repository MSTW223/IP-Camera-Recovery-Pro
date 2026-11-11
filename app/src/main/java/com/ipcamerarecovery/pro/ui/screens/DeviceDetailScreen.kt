package com.ipcamerarecovery.pro.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.R
import com.ipcamerarecovery.pro.model.CameraDevice
import com.ipcamerarecovery.pro.ui.components.SpecItemRow
import com.ipcamerarecovery.pro.ui.components.VendorBadge
import com.ipcamerarecovery.pro.ui.theme.*

@Composable
fun DeviceDetailScreen(
    device: CameraDevice,
    onBack: () -> Unit,
    onNavigateToDiagnostics: (String) -> Unit,
    onNavigateToRecovery: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Back Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.device_details_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Device Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CctvSurface)
                .border(1.dp, CctvBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VendorBadge(manufacturer = device.manufacturer)
                    Text(
                        text = if (device.isActivated) "Active" else "Inactive / Needs Activation",
                        color = if (device.isActivated) StatusSuccess else StatusWarning,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = device.model,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                SpecItemRow("IP Address", device.ipAddress, isMono = true)
                SpecItemRow("MAC Address", device.macAddress, isMono = true)
                SpecItemRow("Serial Number", device.serialNumber, isMono = true)
                SpecItemRow("Firmware Version", device.firmwareVersion)
                SpecItemRow("HTTP / Web Port", "${device.httpPort}", isMono = true)
                SpecItemRow("RTSP Stream Port", "${device.rtspPort}", isMono = true)
                SpecItemRow("Subnet Mask", device.subnetMask, isMono = true)
                SpecItemRow("Default Gateway", device.gateway, isMono = true)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stream URLs
        Text(
            text = "Video Stream & Capture URLs",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CctvSurface)
                .border(1.dp, CctvBorder, RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "RTSP Main Stream", color = TextSecondary, fontSize = 12.sp)
                    IconButton(
                        onClick = { copyToClipboard("RTSP URL", device.getRtspStreamUrl()) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CctvAccent, modifier = Modifier.size(16.dp))
                    }
                }
                Text(
                    text = device.getRtspStreamUrl(),
                    color = CctvAccent,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "HTTP Snapshot Endpoint", color = TextSecondary, fontSize = 12.sp)
                    IconButton(
                        onClick = { copyToClipboard("Snapshot URL", device.getSnapshotUrl()) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CctvAccent, modifier = Modifier.size(16.dp))
                    }
                }
                Text(
                    text = device.getSnapshotUrl(),
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Button(
            onClick = { onNavigateToDiagnostics(device.ipAddress) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = CctvPrimary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Run Connectivity & Port Diagnostics", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(device.getWebUrl()))
                    context.startActivity(browserIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Cannot open browser: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CctvAccent)
        ) {
            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Open Camera Web Admin Portal", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onNavigateToRecovery,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CctvHikvision)
        ) {
            Text("View ${device.manufacturer.displayName} Recovery Guide", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
