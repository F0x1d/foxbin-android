package com.f0x1d.foxbin.repository

import com.f0x1d.foxbin.model.network.request.FoxBinAuthRequestBody
import com.f0x1d.foxbin.repository.base.BaseRepository
import com.f0x1d.foxbin.repository.network.FoxBinService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoxBinAuthRepository @Inject constructor(private val service: FoxBinService): BaseRepository() {

    fun login(username: String, password: String) = apiCallInFlow {
        service.login(FoxBinAuthRequestBody(username, password))
    }

    fun register(username: String, password: String) = apiCallInFlow {
        service.register(FoxBinAuthRequestBody(username, password))
    }
}