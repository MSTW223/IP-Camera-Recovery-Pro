package com.ipcamerarecovery.pro.model

data class PortProbeResult(
    val port: Int,
    val serviceName: String,
    val isOpen: Boolean,
    val responseTimeMs: Long = 0,
    val description: String = ""
)

data class PingDiagnosticResult(
    val targetIp: String,
    val packetsTransmitted: Int = 4,
    val packetsReceived: Int = 4,
    val packetLossPercent: Int = 0,
    val minLatencyMs: Long = 0,
    val avgLatencyMs: Long = 0,
    val maxLatencyMs: Long = 0,
    val jitterMs: Long = 0,
    val isReachable: Boolean = true
)

data class SubnetAuditResult(
    val networkAddress: String,
    val broadcastAddress: String,
    val netmask: String,
    val cidrPrefix: Int,
    val firstUsableIp: String,
    val lastUsableIp: String,
    val totalUsableHosts: Int
)
