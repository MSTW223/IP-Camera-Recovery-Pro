package com.ipcamerarecovery.pro.diagnostics

import com.ipcamerarecovery.pro.model.PingDiagnosticResult
import com.ipcamerarecovery.pro.model.PortProbeResult
import com.ipcamerarecovery.pro.model.SubnetAuditResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.math.abs

class NetworkDiagnosticsEngine {

    suspend fun executePingDiagnostics(targetIp: String, count: Int = 4): PingDiagnosticResult = withContext(Dispatchers.IO) {
        val latencies = mutableListOf<Long>()
        var successCount = 0

        for (i in 1..count) {
            val startTime = System.currentTimeMillis()
            var reachable = false

            try {
                val address = InetAddress.getByName(targetIp)
                reachable = address.isReachable(1000)

                // If ICMP is blocked by firewall, fallback to socket connect on port 80 or 554
                if (!reachable) {
                    val socket = Socket()
                    socket.connect(InetSocketAddress(targetIp, 80), 800)
                    socket.close()
                    reachable = true
                }
            } catch (e: Exception) {
                // try 554 RTSP
                try {
                    val socket = Socket()
                    socket.connect(InetSocketAddress(targetIp, 554), 800)
                    socket.close()
                    reachable = true
                } catch (_: Exception) {}
            }

            val elapsed = System.currentTimeMillis() - startTime
            if (reachable) {
                successCount++
                latencies.add(elapsed)
            }
            kotlinx.coroutines.delay(100)
        }

        if (successCount == 0) {
            return@withContext PingDiagnosticResult(
                targetIp = targetIp,
                packetsTransmitted = count,
                packetsReceived = 0,
                packetLossPercent = 100,
                minLatencyMs = 0,
                avgLatencyMs = 0,
                maxLatencyMs = 0,
                jitterMs = 0,
                isReachable = false
            )
        }

        val min = latencies.minOrNull() ?: 0L
        val max = latencies.maxOrNull() ?: 0L
        val avg = latencies.average().toLong()

        // Calculate jitter (mean absolute deviation)
        val jitter = if (latencies.size > 1) {
            latencies.zipWithNext { a, b -> abs(a - b) }.average().toLong()
        } else 0L

        val lossPercent = ((count - successCount) * 100) / count

        PingDiagnosticResult(
            targetIp = targetIp,
            packetsTransmitted = count,
            packetsReceived = successCount,
            packetLossPercent = lossPercent,
            minLatencyMs = min,
            avgLatencyMs = avg,
            maxLatencyMs = max,
            jitterMs = jitter,
            isReachable = true
        )
    }

    suspend fun auditCctvPorts(targetIp: String): List<PortProbeResult> = withContext(Dispatchers.IO) {
        val standardPorts = listOf(
            Triple(80, "HTTP Web Interface", "Camera configuration web portal"),
            Triple(443, "HTTPS Secure Web", "Encrypted TLS web administration"),
            Triple(554, "RTSP Video Stream", "H.264/H.265 video feed stream"),
            Triple(8000, "Hikvision SDK (Server)", "Hikvision client/CMS control port"),
            Triple(37777, "Dahua SDK (SmartPSS)", "Dahua TCP private command port"),
            Triple(8899, "ONVIF Device Service", "ONVIF Profile S/G/T API"),
            Triple(5000, "UPnP / SSDP Media", "Media discovery service"),
            Triple(23, "Telnet (Legacy Audit)", "Unencrypted terminal - should be closed!")
        )

        standardPorts.map { (port, name, desc) ->
            val startTime = System.currentTimeMillis()
            var isOpen = false
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(targetIp, port), 600)
                socket.close()
                isOpen = true
            } catch (e: Exception) {
                isOpen = false
            }
            val elapsed = System.currentTimeMillis() - startTime

            PortProbeResult(
                port = port,
                serviceName = name,
                isOpen = isOpen,
                responseTimeMs = if (isOpen) elapsed else 0,
                description = desc
            )
        }
    }

    fun calculateSubnet(ipString: String, cidr: Int): SubnetAuditResult {
        val ipParts = ipString.split(".").mapNotNull { it.toIntOrNull() }
        if (ipParts.size != 4) {
            return SubnetAuditResult("0.0.0.0", "0.0.0.0", "255.255.255.0", 24, "0.0.0.1", "0.0.0.254", 254)
        }

        val ipInt = (ipParts[0] shl 24) or (ipParts[1] shl 16) or (ipParts[2] shl 8) or ipParts[3]
        val maskInt = if (cidr == 0) 0 else (-1 shl (32 - cidr))
        val netInt = ipInt and maskInt
        val bcastInt = netInt or maskInt.inv()

        fun intToIp(value: Int): String {
            return "${(value ushr 24) and 0xFF}.${(value ushr 16) and 0xFF}.${(value ushr 8) and 0xFF}.${value and 0xFF}"
        }

        val usableHosts = if (cidr >= 31) 0 else (1 shl (32 - cidr)) - 2
        val firstUsable = if (usableHosts > 0) intToIp(netInt + 1) else intToIp(netInt)
        val lastUsable = if (usableHosts > 0) intToIp(bcastInt - 1) else intToIp(bcastInt)

        return SubnetAuditResult(
            networkAddress = intToIp(netInt),
            broadcastAddress = intToIp(bcastInt),
            netmask = intToIp(maskInt),
            cidrPrefix = cidr,
            firstUsableIp = firstUsable,
            lastUsableIp = lastUsable,
            totalUsableHosts = usableHosts
        )
    }
}
