package com.f0x1d.foxbin.network.model.response

import com.google.gson.annotations.SerializedName

data class FoxBinNotesResponse(@SerializedName("notes") val notes: List<FoxBinNote>)
data class FoxBinNoteResponse(@SerializedName("note") val note: FoxBinNote)

data class FoxBinNote(
    @SerializedName("slug") val slug: String,
    @SerializedName("date") val date: Long,
    @SerializedName("editable") val editable: Boolean? = null,
    @SerializedName("content") val content: String? = null
)
