package com.ipcamerarecovery.pro.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Discovery : Screen("discovery", com.ipcamerarecovery.pro.R.string.nav_discovery, Icons.Default.Videocam)
    object Diagnostics : Screen("diagnostics", com.ipcamerarecovery.pro.R.string.nav_diagnostics, Icons.Default.NetworkCheck)
    object Recovery : Screen("recovery", com.ipcamerarecovery.pro.R.string.nav_recovery, Icons.Default.LockReset)
    object Tools : Screen("tools", com.ipcamerarecovery.pro.R.string.nav_tools, Icons.Default.Build)
    object Settings : Screen("settings", com.ipcamerarecovery.pro.R.string.nav_settings, Icons.Default.Settings)
}
