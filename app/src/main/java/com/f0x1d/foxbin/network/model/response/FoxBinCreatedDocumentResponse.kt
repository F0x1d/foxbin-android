package com.f0x1d.foxbin.network.model.response

import com.google.gson.annotations.SerializedName

data class FoxBinCreatedDocumentResponse(@SerializedName("slug") val slug: String)