package com.ipcamerarecovery.pro

import android.app.Application
import android.util.Log

class IPCApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "IP Camera Recovery Pro initialized - Authorized CCTV Technician Mode Active")
    }

    companion object {
        const val TAG = "IPCameraRecoveryPro"
    }
}
