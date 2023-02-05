package com.f0x1d.foxbin.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.repository.network.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T>(application: Application): AndroidViewModel(application) {
    val loadingStateFlow = MutableStateFlow<LoadingState<T>>(LoadingState.IDLE)

    protected val ctx get() = getApplication<Application>()

    protected suspend fun <T> ResultWrapper<T>.toLoadingState(onSuccess: suspend (ResultWrapper.Success<T>) -> Unit = {}) = when (this) {
        is ResultWrapper.Success<T> -> {
            onSuccess.invoke(this)
            LoadingState.LOADED(value)
        }
        is ResultWrapper.GenericError -> LoadingState.ERROR(error?.error)
        ResultWrapper.NetworkError -> LoadingState.ERROR(ctx.getString(R.string.network_problems))
    }
}

sealed class LoadingState<out T> {
    object IDLE: LoadingState<Nothing>()
    object LOADING: LoadingState<Nothing>()
    data class LOADED<out T>(val data: T): LoadingState<T>()
    data class ERROR(val message: String?): LoadingState<Nothing>()
}