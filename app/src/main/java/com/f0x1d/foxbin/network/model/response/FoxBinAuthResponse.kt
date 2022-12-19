package com.f0x1d.foxbin.network.model.response

import com.google.gson.annotations.SerializedName

data class FoxBinAuthResponse(@SerializedName("accessToken") val accessToken: String)
