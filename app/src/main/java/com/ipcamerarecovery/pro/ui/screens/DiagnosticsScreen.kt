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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamerarecovery.pro.R
import com.ipcamerarecovery.pro.diagnostics.NetworkDiagnosticsEngine
import com.ipcamerarecovery.pro.model.PingDiagnosticResult
import com.ipcamerarecovery.pro.model.PortProbeResult
import com.ipcamerarecovery.pro.model.SubnetAuditResult
import com.ipcamerarecovery.pro.ui.components.SpecItemRow
import com.ipcamerarecovery.pro.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(
    initialIp: String = "192.168.1.108"
) {
    var targetIp by remember { mutableStateOf(initialIp) }
    var isRunning by remember { mutableStateOf(false) }
    var pingResult by remember { mutableStateOf<PingDiagnosticResult?>(null) }
    var portResults by remember { mutableStateOf<List<PortProbeResult>>(emptyList()) }
    var subnetResult by remember { mutableStateOf<SubnetAuditResult?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val diagnosticsEngine = remember { NetworkDiagnosticsEngine() }
    val scrollState = rememberScrollState()

    fun runDiagnostics() {
        if (targetIp.isBlank()) return
        isRunning = true
        coroutineScope.launch {
            pingResult = diagnosticsEngine.executePingDiagnostics(targetIp)
            portResults = diagnosticsEngine.auditCctvPorts(targetIp)
            subnetResult = diagnosticsEngine.calculateSubnet(targetIp, 24)
            isRunning = false
        }
    }

    LaunchedEffect(initialIp) {
        if (initialIp.isNotBlank()) {
            runDiagnostics()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CctvDarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(R.string.diagnostics_title),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Verify latency, open video ports, and IP subnet configuration",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Target IP Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = targetIp,
                onValueChange = { targetIp = it },
                modifier = Modifier.weight(1f),
                label = { Text("Target IP Address", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = CctvPrimary,
                    unfocusedBorderColor = CctvBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    containerColor = CctvSurface
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { runDiagnostics() },
                enabled = !isRunning,
                colors = ButtonDefaults.buttonColors(containerColor = CctvPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(54.dp)
            ) {
                if (isRunning) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                } else {
                    Text("Test", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ping Results Card
        Text(
            text = "ICMP & Connection Diagnostics",
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
            if (pingResult == null) {
                Text("Tap Test to measure connection latency and packet loss", color = TextMuted, fontSize = 12.sp)
            } else {
                val ping = pingResult!!
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (ping.isReachable) "Host Reachable" else "Host Unreachable",
                            color = if (ping.isReachable) StatusSuccess else StatusError,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Loss: ${ping.packetLossPercent}%",
                            color = if (ping.packetLossPercent == 0) StatusSuccess else StatusWarning,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Avg Latency", color = TextMuted, fontSize = 11.sp)
                            Text("${ping.avgLatencyMs} ms", color = CctvAccent, fontSize = 15.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("Min / Max", color = TextMuted, fontSize = 11.sp)
                            Text("${ping.minLatencyMs} / ${ping.maxLatencyMs} ms", color = TextPrimary, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                        }
                        Column {
                            Text("Jitter", color = TextMuted, fontSize = 11.sp)
                            Text("${ping.jitterMs} ms", color = TextPrimary, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Port Scanner Card
        Text(
            text = "CCTV Standard Port Audit",
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
            if (portResults.isEmpty()) {
                Text("Ports not tested yet", color = TextMuted, fontSize = 12.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    portResults.forEach { port ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Port ${port.port}: ${port.serviceName}",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = port.description,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = if (port.isOpen) "OPEN (${port.responseTimeMs}ms)" else "CLOSED",
                                color = if (port.isOpen) StatusSuccess else TextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subnet Information Card
        if (subnetResult != null) {
            val sub = subnetResult!!
            Text(
                text = "Subnet Scope (/24)",
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
                    SpecItemRow("Network Address", sub.networkAddress, isMono = true)
                    SpecItemRow("Broadcast Address", sub.broadcastAddress, isMono = true)
                    SpecItemRow("Usable Range", "${sub.firstUsableIp} - ${sub.lastUsableIp}", isMono = true)
                    SpecItemRow("Usable Hosts", "${sub.totalUsableHosts} devices", isMono = true)
                }
            }
        }
    }
}
