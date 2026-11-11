package com.ipcamerarecovery.pro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import com.ipcamerarecovery.pro.discovery.CameraDiscoveryEngine
import com.ipcamerarecovery.pro.model.CameraDevice
import com.ipcamerarecovery.pro.ui.navigation.Screen
import com.ipcamerarecovery.pro.ui.screens.*
import com.ipcamerarecovery.pro.ui.theme.CctvDarkBg
import com.ipcamerarecovery.pro.ui.theme.CctvPrimary
import com.ipcamerarecovery.pro.ui.theme.IPCameraRecoveryProTheme
import com.ipcamerarecovery.pro.ui.theme.TextMuted
import com.ipcamerarecovery.pro.ui.theme.TextPrimary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var discoveryEngine: CameraDiscoveryEngine

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (!allGranted) {
            Toast.makeText(this, "Local Wi-Fi permissions required for discovery", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        discoveryEngine = CameraDiscoveryEngine(this)
        requestNetworkPermissions()

        setContent {
            var isArabic by remember { mutableStateOf(false) }
            val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                IPCameraRecoveryProTheme {
                    MainAppContent(
                        discoveryEngine = discoveryEngine,
                        isArabic = isArabic,
                        onToggleLanguage = { isArabic = !isArabic }
                    )
                }
            }
        }
    }

    private fun requestNetworkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        val needed = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needed.isNotEmpty()) {
            permissionLauncher.launch(needed.toTypedArray())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    discoveryEngine: CameraDiscoveryEngine,
    isArabic: Boolean,
    onToggleLanguage: () -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Discovery) }
    var selectedDevice by remember { mutableStateOf<CameraDevice?>(null) }
    var diagnosticTargetIp by remember { mutableStateOf("192.168.1.108") }

    val devices = remember { mutableStateListOf<CameraDevice>() }
    var isScanning by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun runScan() {
        if (isScanning) return
        isScanning = true
        devices.clear()
        coroutineScope.launch {
            discoveryEngine.discoverDevices { device ->
                if (devices.none { it.id == device.id || it.ipAddress == device.ipAddress }) {
                    devices.add(device)
                }
            }
            isScanning = false
        }
    }

    // Auto-scan on initial load
    LaunchedEffect(Unit) {
        runScan()
    }

    val screens = listOf(
        Screen.Discovery,
        Screen.Diagnostics,
        Screen.Recovery,
        Screen.Tools,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CctvDarkBg
            ) {
                screens.forEach { screen ->
                    val isSelected = currentScreen == screen && selectedDevice == null
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = {
                            Text(
                                text = if (isArabic) {
                                    when (screen) {
                                        Screen.Discovery -> "الكشف"
                                        Screen.Diagnostics -> "التشخيص"
                                        Screen.Recovery -> "الاستعادة"
                                        Screen.Tools -> "الأدوات"
                                        Screen.Settings -> "الإعدادات"
                                    }
                                } else stringResource(screen.titleResId)
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            selectedDevice = null
                            currentScreen = screen
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CctvPrimary,
                            selectedTextColor = CctvPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = CctvDarkBg
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedDevice != null) {
                DeviceDetailScreen(
                    device = selectedDevice!!,
                    onBack = { selectedDevice = null },
                    onNavigateToDiagnostics = { ip ->
                        diagnosticTargetIp = ip
                        selectedDevice = null
                        currentScreen = Screen.Diagnostics
                    },
                    onNavigateToRecovery = {
                        selectedDevice = null
                        currentScreen = Screen.Recovery
                    }
                )
            } else {
                when (currentScreen) {
                    Screen.Discovery -> DiscoveryScreen(
                        devices = devices,
                        isScanning = isScanning,
                        onStartScan = { runScan() },
                        onDeviceSelected = { device -> selectedDevice = device }
                    )
                    Screen.Diagnostics -> DiagnosticsScreen(
                        initialIp = diagnosticTargetIp
                    )
                    Screen.Recovery -> RecoveryScreen(isArabic = isArabic)
                    Screen.Tools -> ToolsScreen(isArabic = isArabic)
                    Screen.Settings -> SettingsScreen(
                        isArabic = isArabic,
                        onToggleLanguage = onToggleLanguage
                    )
                }
            }
        }
    }
}
