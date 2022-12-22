package com.f0x1d.foxbin.repository.base

import com.f0x1d.foxbin.network.ResultWrapper
import com.f0x1d.foxbin.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {

    protected fun <T> apiCallInFlow(block: suspend () -> T) = apiCallInControllableFlow {
        emit(safeApiCall { block.invoke() })
    }

    protected fun <T> apiCallInControllableFlow(block: suspend FlowCollector<ResultWrapper<T>>.() -> Unit) = flow {
        block.invoke(this)
    }.flowOn(Dispatchers.IO)

    protected suspend fun <T> FlowCollector<ResultWrapper<T>>.processAndUpdateCache(cachePresent: Boolean, cachedData: T?, getAndUpdateBlock: suspend () -> T) {
        if (cachePresent) {
            emit(ResultWrapper.Success(cachedData!!))

            try {
                emit(ResultWrapper.Success(getAndUpdateBlock.invoke()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else emit(
            safeApiCall { getAndUpdateBlock.invoke() }
        )
    }
}