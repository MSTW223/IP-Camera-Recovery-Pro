package com.ipcamerarecovery.pro.model

import java.io.Serializable

data class CameraDevice(
    val id: String,
    val ipAddress: String,
    val macAddress: String,
    val manufacturer: Manufacturer,
    val model: String = "Generic IPC",
    val serialNumber: String = "N/A",
    val firmwareVersion: String = "Unknown",
    val onvifPort: Int = 8899,
    val httpPort: Int = 80,
    val rtspPort: Int = 554,
    val isActivated: Boolean = true,
    val dhcpEnabled: Boolean = true,
    val subnetMask: String = "255.255.255.0",
    val gateway: String = "192.168.1.1",
    val dns: String = "8.8.8.8",
    val discoveryProtocol: DiscoveryProtocol = DiscoveryProtocol.ONVIF_WS_DISCOVERY,
    val discoveryTimestamp: Long = System.currentTimeMillis(),
    val responseLatencyMs: Long = 0,
    val isReachable: Boolean = true
) : Serializable {

    fun getRtspStreamUrl(channel: Int = 1, subtype: Int = 0): String {
        return when (manufacturer) {
            Manufacturer.DAHUA, Manufacturer.IMOU ->
                "rtsp://admin:password@$ipAddress:$rtspPort/cam/realmonitor?channel=$channel&subtype=$subtype"
            Manufacturer.HIKVISION, Manufacturer.EZVIZ -> {
                val streamNumber = if (subtype == 0) "101" else "102"
                "rtsp://admin:password@$ipAddress:$rtspPort/Streaming/Channels/$streamNumber"
            }
            Manufacturer.UNIVIEW ->
                "rtsp://admin:password@$ipAddress:$rtspPort/unicast/c$channel/s$subtype/live"
            Manufacturer.AXIS ->
                "rtsp://admin:password@$ipAddress:$rtspPort/axis-media/media.amp"
            else ->
                "rtsp://admin:password@$ipAddress:$rtspPort/live/ch$channel"
        }
    }

    fun getSnapshotUrl(): String {
        return when (manufacturer) {
            Manufacturer.DAHUA, Manufacturer.IMOU ->
                "http://$ipAddress:$httpPort/cgi-bin/snapshot.cgi"
            Manufacturer.HIKVISION, Manufacturer.EZVIZ ->
                "http://$ipAddress:$httpPort/ISAPI/Streaming/channels/101/picture"
            Manufacturer.UNIVIEW ->
                "http://$ipAddress:$httpPort/images/snapshot.jpg"
            Manufacturer.AXIS ->
                "http://$ipAddress:$httpPort/jpg/image.jpg"
            else ->
                "http://$ipAddress:$httpPort/onvif/snapshot"
        }
    }

    fun getWebUrl(): String {
        return "http://$ipAddress:$httpPort"
    }
}
