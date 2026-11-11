package com.ipcamerarecovery.pro.model

enum class DiscoveryProtocol(val protocolName: String, val defaultPort: Int) {
    HIKVISION_SADP("Hikvision SADP (UDP)", 37020),
    DAHUA_DHIP("Dahua DHIP (UDP)", 37810),
    ONVIF_WS_DISCOVERY("ONVIF WS-Discovery", 3702),
    SUBNET_SWEEP("Subnet Socket Probe", 80),
    MANUAL("Manual Diagnostic Entry", 0)
}
