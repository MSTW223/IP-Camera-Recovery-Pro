package com.ipcamerarecovery.pro.model

data class RecoveryStep(
    val stepNumber: Int,
    val titleEn: String,
    val titleAr: String,
    val descriptionEn: String,
    val descriptionAr: String,
    val warningEn: String? = null,
    val warningAr: String? = null,
    val softwareRequired: String? = null
)

data class RecoveryProcedure(
    val id: String,
    val manufacturer: Manufacturer,
    val titleEn: String,
    val titleAr: String,
    val summaryEn: String,
    val summaryAr: String,
    val officialSoftware: String,
    val steps: List<RecoveryStep>,
    val hardwareButtonInstructionsEn: String? = null,
    val hardwareButtonInstructionsAr: String? = null,
    val distributorEscalationRequired: Boolean = false
)
