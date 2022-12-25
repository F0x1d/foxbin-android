package com.f0x1d.foxbin.database.dao

import androidx.room.*
import com.f0x1d.foxbin.database.entity.FoxBinNote

@Dao
interface FoxBinNoteDao {

    @Query("SELECT * FROM FoxBinNote WHERE my_note = 1 ORDER BY date DESC")
    fun getAllMyNotes(): List<FoxBinNote>

    @Query("SELECT * FROM FoxBinNote WHERE slug = :slug")
    fun getBySlug(slug: String): FoxBinNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: FoxBinNote)

    @Transaction
    fun insertOrUpdateContent(note: FoxBinNote) {
        val cachedNote = getBySlug(note.slug)
        if (cachedNote == null) {
            insert(note)
        } else {
            update(cachedNote.copy(content = note.content))
        }
    }

    @Transaction
    fun updateMyNotes(notes: List<FoxBinNote>) {
        insertAll(notes)
        val updatedSlugs = notes.map { it.slug }

        deleteAll(getAllMyNotes().filterNot { updatedSlugs.contains(it.slug) })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(notes: List<FoxBinNote>)

    @Update
    fun update(note: FoxBinNote)

    @Delete
    fun deleteAll(notes: List<FoxBinNote>)

    @Query("DELETE FROM FoxBinNote WHERE slug = :slug")
    fun deleteBySlug(slug: String)

    @Query("DELETE FROM FoxBinNote")
    fun deleteAll()
}