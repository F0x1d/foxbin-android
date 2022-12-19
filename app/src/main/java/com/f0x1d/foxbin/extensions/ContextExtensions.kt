package com.f0x1d.foxbin.extensions

import android.content.Context
import android.content.Intent
import com.f0x1d.foxbin.FoxBinApp

fun Context.shareNote(slug: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, "${FoxBinApp.FOXBIN_DOMAIN}$slug")
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(intent, null)
    startActivity(shareIntent)
}