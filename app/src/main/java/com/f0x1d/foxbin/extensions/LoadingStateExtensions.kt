package com.f0x1d.foxbin.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.f0x1d.foxbin.ui.widget.ErrorContent
import com.f0x1d.foxbin.viewmodel.base.LoadingState

val <T> LoadingState<T>.isIdle get() = this == LoadingState.IDLE
val <T> LoadingState<T>.isLoading get() = this == LoadingState.LOADING

@Composable
fun <T> LoadingState<T>.IfIdle(content: @Composable () -> Unit) {
    if (isIdle) {
        content.invoke()
    }
}

@Composable
fun <T> LoadingState<T>.IfLoading(content: @Composable () -> Unit) {
    if (isLoading) {
        content.invoke()
    }
}

@Composable
fun <T> LoadingState<T>.DefaultIfLoading() = IfLoading {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun <T> LoadingState<T>.IfLoaded(content: @Composable (T) -> Unit) {
    if (this is LoadingState.LOADED<T>) {
        content.invoke(data)
    }
}

@Composable
fun <T> LoadingState<T>.IfError(content: @Composable (String?) -> Unit) {
    if (this is LoadingState.ERROR) {
        content.invoke(message)
    }
}

@Composable
fun <T> LoadingState<T>.DefaultIfError(onRetryClick: () -> Unit) = IfError {
    ErrorContent(
        error = it,
        onRetryClick = onRetryClick
    )
}