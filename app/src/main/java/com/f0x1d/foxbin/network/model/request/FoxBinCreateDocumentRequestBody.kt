package com.f0x1d.foxbin.network.model.request

import com.google.gson.annotations.SerializedName

data class FoxBinCreateDocumentRequestBody(
    @SerializedName("content") val content: String,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("deleteAfter") val deleteAfter: Int = 0
)