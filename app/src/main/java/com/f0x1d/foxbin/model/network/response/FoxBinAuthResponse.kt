package com.f0x1d.foxbin.model.network.response

import com.google.gson.annotations.SerializedName

data class FoxBinAuthResponse(@SerializedName("accessToken") val accessToken: String)
