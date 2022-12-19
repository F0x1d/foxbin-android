package com.f0x1d.foxbin.repository

import com.f0x1d.foxbin.network.model.request.FoxBinAuthRequestBody
import com.f0x1d.foxbin.network.service.FoxBinService
import com.f0x1d.foxbin.repository.base.BaseRepository
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