package com.ipcamerarecovery.pro.diagnostics

import kotlin.math.atan
import kotlin.math.tan

object CctvCalculators {

    data class StorageEstimate(
        val dailyGigabytesPerCamera: Double,
        val totalGigabytes: Double,
        val totalTerabytes: Double,
        val recommendedHddTb: Int
    )

    fun calculateStorage(
        cameraCount: Int,
        bitrateKbps: Int,
        recordingHoursPerDay: Int,
        retentionDays: Int
    ): StorageEstimate {
        // Bitrate in Kbps -> KB/s -> MB/s
        val bitsPerSecond = bitrateKbps * 1000.0
        val bytesPerSecond = bitsPerSecond / 8.0
        val bytesPerHour = bytesPerSecond * 3600.0
        val bytesPerDayPerCam = bytesPerHour * recordingHoursPerDay
        val gbPerDayPerCam = bytesPerDayPerCam / (1024.0 * 1024.0 * 1024.0)

        val totalGb = gbPerDayPerCam * cameraCount * retentionDays
        val totalTb = totalGb / 1024.0
        val recommendedTb = kotlin.math.ceil(totalTb * 1.15).toInt() // 15% safety headroom

        return StorageEstimate(
            dailyGigabytesPerCamera = String.format("%.2f", gbPerDayPerCam).toDouble(),
            totalGigabytes = String.format("%.2f", totalGb).toDouble(),
            totalTerabytes = String.format("%.2f", totalTb).toDouble(),
            recommendedHddTb = if (recommendedTb < 1) 1 else recommendedTb
        )
    }

    data class LensFovResult(
        val horizontalFovDegrees: Double,
        val verticalFovDegrees: Double,
        val detectDistanceMeters: Double,   // 25 px/m
        val observeDistanceMeters: Double,  // 62.5 px/m
        val recognizeDistanceMeters: Double,// 125 px/m
        val identifyDistanceMeters: Double  // 250 px/m
    )

    fun calculateLensFov(
        focalLengthMm: Double,
        sensorWidthMm: Double = 5.27, // standard 1/2.8" sensor width
        sensorHeightMm: Double = 2.96,
        horizontalResolutionPx: Int = 2560 // 4MP
    ): LensFovResult {
        val hFovRad = 2.0 * atan(sensorWidthMm / (2.0 * focalLengthMm))
        val hFovDeg = Math.toDegrees(hFovRad)

        val vFovRad = 2.0 * atan(sensorHeightMm / (2.0 * focalLengthMm))
        val vFovDeg = Math.toDegrees(vFovRad)

        // DORI calculations based on EN 62676-4 standard:
        // Detection = 25 px/m
        // Observation = 62.5 px/m
        // Recognition = 125 px/m
        // Identification = 250 px/m
        val detectDist = (focalLengthMm * horizontalResolutionPx) / (sensorWidthMm * 25.0)
        val observeDist = (focalLengthMm * horizontalResolutionPx) / (sensorWidthMm * 62.5)
        val recognizeDist = (focalLengthMm * horizontalResolutionPx) / (sensorWidthMm * 125.0)
        val identifyDist = (focalLengthMm * horizontalResolutionPx) / (sensorWidthMm * 250.0)

        return LensFovResult(
            horizontalFovDegrees = String.format("%.1f", hFovDeg).toDouble(),
            verticalFovDegrees = String.format("%.1f", vFovDeg).toDouble(),
            detectDistanceMeters = String.format("%.1f", detectDist).toDouble(),
            observeDistanceMeters = String.format("%.1f", observeDist).toDouble(),
            recognizeDistanceMeters = String.format("%.1f", recognizeDist).toDouble(),
            identifyDistanceMeters = String.format("%.1f", identifyDist).toDouble()
        )
    }
}
