package com.f0x1d.foxbin.repository

import com.f0x1d.foxbin.network.model.request.FoxBinCreateDocumentRequestBody
import com.f0x1d.foxbin.network.service.FoxBinService
import com.f0x1d.foxbin.repository.base.BaseRepository
import com.f0x1d.foxbin.store.datastore.UserDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoxBinNotesRepository @Inject constructor(
    private val service: FoxBinService,
    private val userDataStore: UserDataStore
): BaseRepository() {

    fun create(content: String, slug: String?) = apiCallInFlow {
        service.create(
            FoxBinCreateDocumentRequestBody(
                content,
                userDataStore.accessToken.first(),
                slug?.ifEmpty { null }
            )
        ).slug
    }

    fun edit(content: String, slug: String) = apiCallInFlow {
        service.edit(
            FoxBinCreateDocumentRequestBody(
                content,
                userDataStore.accessToken.first(),
                slug
            )
        ).slug
    }

    fun getAll(accessToken: String) = apiCallInFlow {
        service.getAll(accessToken).notes
    }

    fun get(slug: String) = apiCallInFlow {
        service.get(slug, userDataStore.accessToken.first()).note
    }

    fun delete(slug: String) = apiCallInFlow {
        service.delete(slug, userDataStore.accessToken.first() ?: return@apiCallInFlow)
    }
}