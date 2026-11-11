package com.ipcamerarecovery.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.ipcamerarecovery.pro.ui.components.SpecItemRow
import com.ipcamerarecovery.pro.ui.theme.*

@Composable
fun SettingsScreen(
    isArabic: Boolean,
    onToggleLanguage: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = if (isArabic) "الإعدادات ومعلومات الإصدار" else stringResource(R.string.nav_settings),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isArabic) "بيانات التوقيع والشهادة وتفضيلات اللغة" else "Release signing certificate identity, application ID and preferences",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language Switch Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CctvSurface)
                .border(1.dp, CctvBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = CctvAccent)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isArabic) "اللغة الحالية: العربية (RTL)" else "Interface Language",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isArabic) "التبديل إلى الإنجليزية" else "Switch to Arabic (Full RTL)",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Button(
                    onClick = onToggleLanguage,
                    colors = ButtonDefaults.buttonColors(containerColor = CctvPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isArabic) "English" else "العربية", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Production Signing Certificate Details
        Text(
            text = if (isArabic) "بيانات توقيع حزمة الإنتاج الرسمية (Release Signing)" else "Production Release Signing Certificate",
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
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Build Type: PRODUCTION RELEASE",
                        color = StatusSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                SpecItemRow("Application ID", "com.ipcamerarecovery.pro", isMono = true)
                SpecItemRow("Version Name", "1.0.0", isMono = true)
                SpecItemRow("Version Code", "100", isMono = true)
                SpecItemRow("Min SDK", "API 26 (Android 8.0 Oreo)", isMono = true)
                SpecItemRow("Target SDK", "API 34 (Android 14)", isMono = true)
                SpecItemRow("Key Alias", "ipcamera_release", isMono = true)
                SpecItemRow("Key Algorithm", "RSA 2048-bit (SHA256withRSA)", isMono = true)
                SpecItemRow("Certificate Validity", "Until March 28, 2054", isMono = true)

                Divider(color = CctvBorder, modifier = Modifier.padding(vertical = 10.dp))

                Text("SHA-256 Certificate Fingerprint:", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "CD:39:CC:CB:6B:39:DE:3B:8F:DF:3B:EC:DB:98:64:ED:79:3E:E1:F2:46:D2:AE:67:4B:36:AD:1D:4C:41:CE:3F",
                    color = CctvAccent,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Future Release Update Instructions
        Text(
            text = if (isArabic) "تعليمات تحديث التطبيق للإصدارات القادمة" else "Instructions for Future Releases & Key Preservation",
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
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (isArabic)
                        "لضمان إمكانية تحديث التطبيق وتثبيته فوق الإصدار السابق دون فقدان بيانات أو تعارض في التوقيع:\n\n" +
                        "1. احتفظ دائماً بملف المفتاح release.jks في مسار آمن ونسخة احتياطية مشفرة.\n" +
                        "2. في أي إصدار جديد، قم برفع versionCode في app/build.gradle.kts (مثلاً: 101, 102).\n" +
                        "3. استخدم نفس الاسم المستعار (ipcamera_release) ونفس كلمة المرور.\n" +
                        "4. لا تُنشئ أبداً keystore جديد لنفس التطبيق وإلا سيرفض أندرويد تثبيت التحديث."
                    else
                        "To ensure seamless updates on user devices without signature mismatch errors:\n\n" +
                        "1. Store release.jks in a secure private repository or encrypted vault.\n" +
                        "2. For every future release, increment 'versionCode' in app/build.gradle.kts (e.g., 101, 102) and update 'versionName'.\n" +
                        "3. Sign with the identical key alias 'ipcamera_release'.\n" +
                        "4. Never generate a new keystore for com.ipcamerarecovery.pro, or Android OS will reject the update package.",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
