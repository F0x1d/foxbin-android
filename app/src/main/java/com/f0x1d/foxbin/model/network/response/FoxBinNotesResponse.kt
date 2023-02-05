package com.f0x1d.foxbin.model.network.response

import com.f0x1d.foxbin.database.entity.FoxBinNote
import com.google.gson.annotations.SerializedName

data class FoxBinNotesResponse(@SerializedName("notes") val notes: List<FoxBinNote>)
data class FoxBinNoteResponse(@SerializedName("note") val note: FoxBinNote)