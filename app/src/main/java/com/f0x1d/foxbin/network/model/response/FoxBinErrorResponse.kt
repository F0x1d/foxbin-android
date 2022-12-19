package com.f0x1d.foxbin.network.model.response

import com.google.gson.annotations.SerializedName

data class FoxBinErrorResponse(@SerializedName("error") val error: String)
