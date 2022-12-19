package com.f0x1d.foxbin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoxBinApp: Application() {

    companion object {
        const val FOXBIN_DOMAIN = "https://foxbin.f0x1d.com/"
    }
}