package com.f0x1d.foxbin.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.f0x1d.foxbin.repository.FoxBinNotesRepository
import com.f0x1d.foxbin.repository.network.ResultWrapper
import com.f0x1d.foxbin.viewmodel.base.BaseViewModel
import com.f0x1d.foxbin.viewmodel.base.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    application: Application,
    private val notesRepository: FoxBinNotesRepository
): BaseViewModel<String>(application) {

    fun publish(slug: String, content: String, onDone: (String) -> Unit) = changeContent(notesRepository.create(content, slug), onDone)
    fun edit(slug: String, content: String, onDone: (String) -> Unit) = changeContent(notesRepository.edit(content, slug), onDone)

    private fun changeContent(flow: Flow<ResultWrapper<String>>, onDone: (String) -> Unit) = viewModelScope.launch {
        loadingStateFlow.update { LoadingState.LOADING }

        flow.collect { result ->
            loadingStateFlow.update {
                result.toLoadingState {
                    onDone.invoke(it.value)
                }
            }
        }
    }

    fun retry() = viewModelScope.launch {
        loadingStateFlow.update { LoadingState.IDLE }
    }
}