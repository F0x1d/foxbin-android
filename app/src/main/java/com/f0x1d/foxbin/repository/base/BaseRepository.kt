package com.f0x1d.foxbin.repository.base

import com.f0x1d.foxbin.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {

    protected fun <T> apiCallInFlow(block: suspend () -> T) = flow {
        emit(safeApiCall { block.invoke() })
    }.flowOn(Dispatchers.IO)
}