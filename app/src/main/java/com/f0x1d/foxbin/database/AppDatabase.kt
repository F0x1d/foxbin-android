package com.f0x1d.foxbin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.f0x1d.foxbin.database.dao.FoxBinNoteDao
import com.f0x1d.foxbin.database.entity.FoxBinNote

@Database(entities = [FoxBinNote::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun notesDao(): FoxBinNoteDao
}