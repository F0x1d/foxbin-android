package com.f0x1d.foxbin.model.network.request

import com.google.gson.annotations.SerializedName

data class FoxBinAuthRequestBody(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
