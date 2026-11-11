package com.ipcamerarecovery.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.R
import com.ipcamerarecovery.pro.model.RecoveryProcedure
import com.ipcamerarecovery.pro.model.RecoveryStep
import com.ipcamerarecovery.pro.recovery.OfficialRecoveryGuides
import com.ipcamerarecovery.pro.ui.components.VendorBadge
import com.ipcamerarecovery.pro.ui.theme.*

@Composable
fun RecoveryScreen(isArabic: Boolean = false) {
    val procedures = OfficialRecoveryGuides.PROCEDURES
    var selectedIndex by remember { mutableStateOf(0) }
    val currentProcedure = procedures[selectedIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
    ) {
        Text(
            text = if (isArabic) "إجراءات الاستعادة الرسمية" else stringResource(R.string.recovery_title),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isArabic) "دليل معتمد لإعادة ضبط كلمات المرور دون كسر حماية" else stringResource(R.string.recovery_subtitle),
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Ethical Compliance Notice Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0x22F59E0B))
                .border(1.dp, Color(0x66F59E0B), RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = CctvHikvision,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isArabic)
                        "تنبيه الامتثال الأخلاقي: هذا المساعد يتبع حصرياً الإجراءات المعتمدة من المصنع ولا يقوم بأي عمليات تخمين أو تجاوز أمني. مخصص للفنيين المصرح لهم فقط."
                    else
                        "STRICT COMPLIANCE NOTICE: This assistant strictly follows authorized manufacturer reset procedures. It NEVER attempts brute-force attacks or security exploits. Authorized technicians only.",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Procedure Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            procedures.forEachIndexed { index, proc ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedIndex == index) CctvPrimary else CctvSurface)
                        .border(1.dp, if (selectedIndex == index) CctvAccent else CctvBorder, RoundedCornerShape(8.dp))
                        .clickable { selectedIndex = index }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) {
                            when (index) {
                                0 -> "هايكفيجن"
                                1 -> "داهوا"
                                else -> "أزرار الهاردوير"
                            }
                        } else {
                            when (index) {
                                0 -> "Hikvision"
                                1 -> "Dahua"
                                else -> "HW Buttons"
                            }
                        },
                        color = if (selectedIndex == index) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Active Procedure Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VendorBadge(manufacturer = currentProcedure.manufacturer)
                            Text(
                                text = currentProcedure.officialSoftware,
                                color = CctvAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isArabic) currentProcedure.titleAr else currentProcedure.titleEn,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isArabic) currentProcedure.summaryAr else currentProcedure.summaryEn,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Steps
            items(currentProcedure.steps) { step ->
                StepCardItem(step = step, isArabic = isArabic)
            }

            // Hardware Button Instructions Note
            if (currentProcedure.hardwareButtonInstructionsEn != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E1B4B))
                            .border(1.dp, Color(0xFF4338CA), RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isArabic) "تعليمات الزر الفيزيائي للكاميرا" else "Physical Reset Switch Instructions",
                                color = Color(0xFFA5B4FC),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isArabic) currentProcedure.hardwareButtonInstructionsAr ?: "" else currentProcedure.hardwareButtonInstructionsEn ?: "",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepCardItem(step: RecoveryStep, isArabic: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CctvSurface)
            .border(1.dp, CctvBorder, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(CctvPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isArabic) step.titleAr else step.titleEn,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isArabic) step.descriptionAr else step.descriptionEn,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                if (step.warningEn != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) step.warningAr ?: "" else step.warningEn,
                            color = StatusWarning,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
