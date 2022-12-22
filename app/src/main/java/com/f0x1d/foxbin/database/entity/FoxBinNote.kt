package com.f0x1d.foxbin.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["slug"], indices = [Index(value = ["slug"], unique = true)])
data class FoxBinNote(
    @SerializedName("slug") @ColumnInfo(name = "slug") val slug: String = "",
    @SerializedName("date") @ColumnInfo(name = "date") val date: Long = 0L,
    @SerializedName("editable") @ColumnInfo(name = "editable") val editable: Boolean? = null,
    @SerializedName("content") @ColumnInfo(name = "content") val content: String? = null,
    @ColumnInfo(name = "my_note") val myNote: Boolean = true
)