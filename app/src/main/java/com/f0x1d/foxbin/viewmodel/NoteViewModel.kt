package com.f0x1d.foxbin.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.f0x1d.foxbin.network.model.response.FoxBinNote
import com.f0x1d.foxbin.repository.FoxBinNotesRepository
import com.f0x1d.foxbin.viewmodel.base.BaseViewModel
import com.f0x1d.foxbin.viewmodel.base.LoadingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel @AssistedInject constructor(
    application: Application,
    @Assisted private val slug: String,
    private val notesRepository: FoxBinNotesRepository
): BaseViewModel<FoxBinNote>(application) {

    companion object {
        fun provideFactory(assistedFactory: NoteViewModelAssistedFactory, slug: String) = viewModelFactory {
            initializer {
                assistedFactory.create(slug)
            }
        }
    }

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        loadingStateFlow.update { LoadingState.LOADING }

        notesRepository.get(slug).collect { result ->
            loadingStateFlow.update {
                result.toLoadingState()
            }
        }
    }
}

@AssistedFactory
interface NoteViewModelAssistedFactory {
    fun create(slug: String): NoteViewModel
}