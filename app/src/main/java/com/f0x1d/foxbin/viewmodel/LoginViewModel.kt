package com.f0x1d.foxbin.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.f0x1d.foxbin.database.AppDatabase
import com.f0x1d.foxbin.model.network.response.FoxBinAuthResponse
import com.f0x1d.foxbin.repository.FoxBinAuthRepository
import com.f0x1d.foxbin.repository.network.ResultWrapper
import com.f0x1d.foxbin.store.datastore.UserDataStore
import com.f0x1d.foxbin.viewmodel.base.BaseViewModel
import com.f0x1d.foxbin.viewmodel.base.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val userDataStore: UserDataStore,
    private val authRepository: FoxBinAuthRepository,
    private val database: AppDatabase
): BaseViewModel<FoxBinAuthResponse>(application) {

    fun login(username: String, password: String, onDone: () -> Unit) = auth(authRepository.login(username, password), username, onDone)
    fun register(username: String, password: String, onDone: () -> Unit) = auth(authRepository.register(username, password), username, onDone)

    private fun auth(flow: Flow<ResultWrapper<FoxBinAuthResponse>>, username: String, onDone: () -> Unit) = viewModelScope.launch {
        loadingStateFlow.update { LoadingState.LOADING }

        flow.collect { result ->
            loadingStateFlow.update {
                result.toLoadingState {
                    withContext(Dispatchers.IO) {
                        database.notesDao().deleteAll()
                    }

                    userDataStore.apply {
                        saveAccessToken(it.value.accessToken)
                        saveUsername(username)
                    }

                    onDone.invoke()
                }
            }
        }
    }

    fun retry() = viewModelScope.launch {
        loadingStateFlow.update { LoadingState.IDLE }
    }
}