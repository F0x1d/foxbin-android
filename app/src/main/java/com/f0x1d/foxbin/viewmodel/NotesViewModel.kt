package com.f0x1d.foxbin.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.network.ResultWrapper
import com.f0x1d.foxbin.network.model.response.FoxBinNote
import com.f0x1d.foxbin.repository.FoxBinNotesRepository
import com.f0x1d.foxbin.store.datastore.UserDataStore
import com.f0x1d.foxbin.viewmodel.base.BaseViewModel
import com.f0x1d.foxbin.viewmodel.base.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    application: Application,
    private val userDataStore: UserDataStore,
    private val notesRepository: FoxBinNotesRepository
): BaseViewModel<List<FoxBinNote>>(application) {

    val accessTokenFlow = userDataStore.accessToken
    val usernameFlow = userDataStore.username

    init {
        observeTokenChanges()
    }

    fun reload() = viewModelScope.launch {
        load(accessTokenFlow.first() ?: return@launch)
    }

    fun logout() = viewModelScope.launch {
        userDataStore.apply {
            saveAccessToken(null)
            saveUsername(null)
        }
    }

    fun delete(slug: String) = viewModelScope.launch {
        notesRepository.delete(slug).collect { result ->
            loadingStateFlow.update {
                when (result) {
                    is ResultWrapper.GenericError -> LoadingState.ERROR(result.error?.error)
                    ResultWrapper.NetworkError -> LoadingState.ERROR(ctx.getString(R.string.network_problems))

                    else -> {
                        if (it is LoadingState.LOADED<List<FoxBinNote>>) {
                            LoadingState.LOADED(it.data.filterNot { note -> note.slug == slug })
                        } else it
                    }
                }
            }
        }
    }

    private fun observeTokenChanges() = viewModelScope.launch {
        accessTokenFlow.collect { accessToken ->
            if (accessToken == null) {
                loadingStateFlow.update { LoadingState.LOADED(emptyList()) }
                return@collect
            }

            load(accessToken)
        }
    }

    private suspend fun load(accessToken: String) {
        if (loadingStateFlow.value == LoadingState.LOADING) return

        loadingStateFlow.update { LoadingState.LOADING }

        notesRepository.getAll(accessToken).collect { result ->
            loadingStateFlow.update {
                result.toLoadingState()
            }
        }
    }
}