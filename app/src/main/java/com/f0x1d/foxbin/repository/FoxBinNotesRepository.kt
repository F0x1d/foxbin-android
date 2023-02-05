package com.f0x1d.foxbin.repository

import com.f0x1d.foxbin.database.AppDatabase
import com.f0x1d.foxbin.database.entity.FoxBinNote
import com.f0x1d.foxbin.model.network.request.FoxBinCreateDocumentRequestBody
import com.f0x1d.foxbin.repository.base.BaseRepository
import com.f0x1d.foxbin.repository.network.FoxBinService
import com.f0x1d.foxbin.store.datastore.UserDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoxBinNotesRepository @Inject constructor(
    private val service: FoxBinService,
    private val userDataStore: UserDataStore,
    private val database: AppDatabase
): BaseRepository() {

    fun create(content: String, slug: String?) = apiCallInFlow {
        val accessToken = userDataStore.accessToken.first()

        service.create(
            FoxBinCreateDocumentRequestBody(
                content,
                accessToken,
                slug?.ifEmpty { null }
            )
        ).slug.also { createNote(it, content, accessToken != null) }
    }

    private fun createNote(slug: String, content: String, editable: Boolean) {
        database.notesDao().insert(
            FoxBinNote(
                slug,
                System.currentTimeMillis(),
                editable,
                content,
                true
            )
        )
    }

    fun edit(content: String, slug: String) = apiCallInFlow {
        service.edit(
            FoxBinCreateDocumentRequestBody(
                content,
                userDataStore.accessToken.first(),
                slug
            )
        ).slug.also {
            val savedNote = database.notesDao().getBySlug(slug) ?: return@also
            database.notesDao().update(savedNote.copy(content = content))
        }
    }

    fun getAll() = apiCallInControllableFlow {
        val savedNotes = database.notesDao().getAllMyNotes()

        processAndUpdateCache(
            savedNotes.isNotEmpty(),
            savedNotes
        ) {
            val accessToken = userDataStore.accessToken.first()

            if (accessToken == null)
                savedNotes
            else
                service.getAll(accessToken).notes.also {
                    database.notesDao().updateMyNotes(it)
                }
        }
    }

    fun get(slug: String) = apiCallInControllableFlow {
        val accessToken = userDataStore.accessToken.first()

        val savedNote = database.notesDao().getBySlug(slug)

        processAndUpdateCache(
            savedNote?.content != null && savedNote.content.isNotEmpty(),
            savedNote
        ) {
            service.get(slug, accessToken).note.also {
                database.notesDao().insertOrUpdateContent(
                    it.copy(
                        myNote = if (accessToken == null) false else (it.editable == true)
                    )
                )
            }
        }
    }

    fun delete(slug: String, accessToken: String) = apiCallInFlow {
        service.delete(slug, accessToken)
        database.notesDao().deleteBySlug(slug)
    }
}