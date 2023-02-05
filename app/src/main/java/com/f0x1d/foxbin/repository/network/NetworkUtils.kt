package com.f0x1d.foxbin.repository.network

import com.f0x1d.foxbin.model.network.response.FoxBinErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: FoxBinErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T) = try {
    ResultWrapper.Success(apiCall.invoke())
} catch (throwable: Throwable) {
    when (throwable) {
        is IOException -> ResultWrapper.NetworkError
        is HttpException -> {
            val code = throwable.code()
            val errorResponse = convertErrorBody(throwable)

            ResultWrapper.GenericError(code, errorResponse)
        }
        else -> ResultWrapper.GenericError(null, null)
    }
}

private fun convertErrorBody(throwable: HttpException) = try {
    throwable.response()?.errorBody()?.source()?.let {
        Gson().fromJson(it.readUtf8(), FoxBinErrorResponse::class.java)
    }
} catch (exception: Exception) {
    null
}