package com.f0x1d.foxbin.model.network.response

import com.google.gson.annotations.SerializedName

data class FoxBinCreatedDocumentResponse(@SerializedName("slug") val slug: String)