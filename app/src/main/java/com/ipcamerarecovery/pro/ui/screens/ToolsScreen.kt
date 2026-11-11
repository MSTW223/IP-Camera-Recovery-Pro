package com.ipcamerarecovery.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.R
import com.ipcamerarecovery.pro.diagnostics.CctvCalculators
import com.ipcamerarecovery.pro.ui.components.SpecItemRow
import com.ipcamerarecovery.pro.ui.theme.*

@Composable
fun ToolsScreen(isArabic: Boolean = false) {
    val scrollState = rememberScrollState()

    // Storage Calculator State
    var cameraCount by remember { mutableStateOf(8f) }
    var bitrateKbps by remember { mutableStateOf(4096f) } // 4 Mbps per cam (typical 4MP H.265)
    var retentionDays by remember { mutableStateOf(30f) }

    val storageEstimate = remember(cameraCount, bitrateKbps, retentionDays) {
        CctvCalculators.calculateStorage(
            cameraCount = cameraCount.toInt(),
            bitrateKbps = bitrateKbps.toInt(),
            recordingHoursPerDay = 24,
            retentionDays = retentionDays.toInt()
        )
    }

    // Lens FOV State
    var focalLengthMm by remember { mutableStateOf(4.0f) }
    val lensFov = remember(focalLengthMm) {
        CctvCalculators.calculateLensFov(focalLengthMm.toDouble())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = if (isArabic) "أدوات مهندس كاميرات المراقبة" else stringResource(R.string.tools_title),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isArabic) "حاسبات هندسية لسعة التخزين والباندويث وزوايا العدسات" else "Field calculation utilities for storage and lens DORI specifications",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Storage Calculator Card
        Text(
            text = if (isArabic) "حاسبة التخزين والباندويث" else "Storage & Bandwidth Calculator",
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CctvSurface)
                .border(1.dp, CctvBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isArabic) "عدد الكاميرات" else "Camera Count", color = TextSecondary, fontSize = 12.sp)
                    Text("${cameraCount.toInt()} Cameras", color = CctvAccent, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                }
                Slider(
                    value = cameraCount,
                    onValueChange = { cameraCount = it },
                    valueRange = 1f..64f,
                    steps = 63,
                    colors = SliderDefaults.colors(thumbColor = CctvPrimary, activeTrackColor = CctvAccent)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isArabic) "معدل البت (Bitrate)" else "Bitrate (Kbps)", color = TextSecondary, fontSize = 12.sp)
                    Text("${bitrateKbps.toInt()} Kbps (${bitrateKbps.toInt() / 1024} Mbps)", color = CctvAccent, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                }
                Slider(
                    value = bitrateKbps,
                    onValueChange = { bitrateKbps = it },
                    valueRange = 1024f..16384f,
                    colors = SliderDefaults.colors(thumbColor = CctvPrimary, activeTrackColor = CctvAccent)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isArabic) "أيام الاحتفاظ" else "Retention Period", color = TextSecondary, fontSize = 12.sp)
                    Text("${retentionDays.toInt()} Days", color = CctvAccent, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                }
                Slider(
                    value = retentionDays,
                    onValueChange = { retentionDays = it },
                    valueRange = 7f..90f,
                    colors = SliderDefaults.colors(thumbColor = CctvPrimary, activeTrackColor = CctvAccent)
                )

                Divider(color = CctvBorder, modifier = Modifier.padding(vertical = 10.dp))

                SpecItemRow(
                    if (isArabic) "إجمالي السعة المطلوبة" else "Required Storage",
                    "${storageEstimate.totalTerabytes} TB (${storageEstimate.totalGigabytes} GB)",
                    isMono = true
                )
                SpecItemRow(
                    if (isArabic) "القرص الموصى به (+15% أمان)" else "Recommended Surveillance HDD",
                    "${storageEstimate.recommendedHddTb} TB Western Digital Purple / SkyHawk",
                    isMono = true
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Lens & DORI Calculator Card
        Text(
            text = if (isArabic) "حاسبة زاوية العدسة ومسافات DORI" else "Lens Focal Length & DORI Calculator",
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CctvSurface)
                .border(1.dp, CctvBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isArabic) "البعد البؤري للعدسة" else "Lens Focal Length", color = TextSecondary, fontSize = 12.sp)
                    Text("${String.format("%.1f", focalLengthMm)} mm", color = CctvAccent, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                }
                Slider(
                    value = focalLengthMm,
                    onValueChange = { focalLengthMm = it },
                    valueRange = 2.8f..16.0f,
                    colors = SliderDefaults.colors(thumbColor = CctvPrimary, activeTrackColor = CctvAccent)
                )

                Divider(color = CctvBorder, modifier = Modifier.padding(vertical = 10.dp))

                SpecItemRow(if (isArabic) "زاوية الرؤية الأفقية" else "Horizontal FOV", "${lensFov.horizontalFovDegrees}°", isMono = true)
                SpecItemRow(if (isArabic) "زاوية الرؤية الرأسية" else "Vertical FOV", "${lensFov.verticalFovDegrees}°", isMono = true)
                SpecItemRow(if (isArabic) "مسافة الكشف (Detect 25px/m)" else "Detect (25 px/m)", "${lensFov.detectDistanceMeters} m", isMono = true)
                SpecItemRow(if (isArabic) "مسافة المراقبة (Observe 62.5px/m)" else "Observe (62.5 px/m)", "${lensFov.observeDistanceMeters} m", isMono = true)
                SpecItemRow(if (isArabic) "مسافة التعرف (Recognize 125px/m)" else "Recognize (125 px/m)", "${lensFov.recognizeDistanceMeters} m", isMono = true)
                SpecItemRow(if (isArabic) "مسافة التحديد الدقيق (Identify 250px/m)" else "Identify (250 px/m)", "${lensFov.identifyDistanceMeters} m", isMono = true)
            }
        }
    }
}
