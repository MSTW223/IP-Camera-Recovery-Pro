package com.ipcamerarecovery.pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.model.Manufacturer
import com.ipcamerarecovery.pro.ui.theme.CctvDahua
import com.ipcamerarecovery.pro.ui.theme.CctvHikvision
import com.ipcamerarecovery.pro.ui.theme.CctvOnvif
import com.ipcamerarecovery.pro.ui.theme.CctvPrimary
import com.ipcamerarecovery.pro.ui.theme.TextMuted
import com.ipcamerarecovery.pro.ui.theme.TextPrimary
import com.ipcamerarecovery.pro.ui.theme.TextSecondary

@Composable
fun VendorBadge(manufacturer: Manufacturer) {
    val (bgColor, textColor, borderCol) = when (manufacturer) {
        Manufacturer.HIKVISION -> Triple(Color(0x22F59E0B), CctvHikvision, Color(0x66F59E0B))
        Manufacturer.DAHUA -> Triple(Color(0x22EF4444), CctvDahua, Color(0x66EF4444))
        Manufacturer.UNIVIEW -> Triple(Color(0x2238BDF8), Color(0xFF38BDF8), Color(0x6638BDF8))
        else -> Triple(Color(0x2210B981), CctvOnvif, Color(0x6610B981))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderCol, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = manufacturer.displayName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SpecItemRow(label: String, value: String, isMono: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertling
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default
        )
    }
}
