
package com.ipcamerarecovery.pro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CameraRecoveryScreen { message ->
                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

private val Background = Color(0xFF0B1220)
private val Panel = Color(0xFF172337)
private val Blue = Color(0xFF29B6F6)
private val Green = Color(0xFF66BB6A)

@Composable
fun CameraRecoveryScreen(
    onAction: (String) -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            background = Background,
            surface = Panel
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                Text(
                    "IP CAMERA",
                    color = Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Recovery Pro",
                    color = Color.White,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "أداة فحص واستعادة أجهزة المراقبة",
                    color = Color.LightGray,
                    fontSize = 15.sp
                )

                HorizontalDivider(
                    color = Color.DarkGray
                )

                Text(
                    "لوحة التحكم الرئيسية",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                ActionCard(
                    title = "اكتشاف كاميرات الشبكة",
                    description = "البحث عن أجهزة IP المتصلة بالشبكة المحلية",
                    emoji = "🔍",
                    onClick = {
                        onAction("سيتم تفعيل محرك اكتشاف الأجهزة في المرحلة الثانية")
                    }
                )

                ActionCard(
                    title = "Dahua",
                    description = "فحص الأجهزة وإجراءات استعادة الوصول الرسمية",
                    emoji = "📹",
                    onClick = {
                        onAction("قسم Dahua قيد التطوير")
                    }
                )

                ActionCard(
                    title = "Hikvision",
                    description = "فحص الأجهزة وإجراءات الاستعادة الرسمية",
                    emoji = "🎥",
                    onClick = {
                        onAction("قسم Hikvision قيد التطوير")
                    }
                )

                ActionCard(
                    title = "معلومات الأجهزة",
                    description = "عنوان IP والموديل والرقم التسلسلي وحالة الاتصال",
                    emoji = "🖥️",
                    onClick = {
                        onAction("سيتم تفعيل معلومات الأجهزة لاحقاً")
                    }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Panel
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "حالة النظام",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .background(
                                        Green,
                                        RoundedCornerShape(50)
                                    )
                            )

                            Text(
                                "التطبيق يعمل",
                                color = Green
                            )
                        }

                        Text(
                            "عدد الأجهزة المكتشفة: 0",
                            color = Color.LightGray
                        )

                        Text(
                            "الإصدار: 1.0.0",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    "IP Camera Recovery Pro",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Panel
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                emoji,
                fontSize = 30.sp
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    description,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
