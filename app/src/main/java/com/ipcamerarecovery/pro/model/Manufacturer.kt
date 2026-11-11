package com.ipcamerarecovery.pro.model

enum class Manufacturer(val displayName: String, val brandCode: String) {
    HIKVISION("Hikvision", "HIK"),
    DAHUA("Dahua Technology", "DH"),
    UNIVIEW("Uniview (UNV)", "UNV"),
    AXIS("Axis Communications", "AXIS"),
    IMOU("Imou (Dahua Consumer)", "IMOU"),
    EZVIZ("EZVIZ (Hikvision Consumer)", "EZVIZ"),
    GENERIC_ONVIF("ONVIF Standard Device", "ONVIF"),
    UNKNOWN("Network Device", "NET");

    companion object {
        fun fromMacOUI(mac: String): Manufacturer {
            val clean = mac.replace(":", "").replace("-", "").uppercase()
            if (clean.length < 6) return UNKNOWN
            val prefix = clean.substring(0, 6)

            return when {
                // Hikvision Known OUIs
                prefix.startsWith("BC5ECD") || prefix.startsWith("4419B6") ||
                prefix.startsWith("001A1E") || prefix.startsWith("D46E0E") ||
                prefix.startsWith("C056E3") || prefix.startsWith("54C415") ||
                prefix.startsWith("2857BE") || prefix.startsWith("10DA43") -> HIKVISION

                // Dahua Known OUIs
                prefix.startsWith("38AF29") || prefix.startsWith("E0508B") ||
                prefix.startsWith("9C1463") || prefix.startsWith("4C11BF") ||
                prefix.startsWith("70B3D5") || prefix.startsWith("A0BDCD") ||
                prefix.startsWith("14A78B") || prefix.startsWith("B4A94F") -> DAHUA

                // Uniview
                prefix.startsWith("6C8D77") || prefix.startsWith("CC54B7") -> UNIVIEW

                // Axis
                prefix.startsWith("00408C") || prefix.startsWith("ACC8E4") -> AXIS

                else -> UNKNOWN
            }
        }
    }
}
