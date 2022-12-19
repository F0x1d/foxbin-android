package com.f0x1d.foxbin.network.service

import com.f0x1d.foxbin.network.model.request.FoxBinAuthRequestBody
import com.f0x1d.foxbin.network.model.request.FoxBinCreateDocumentRequestBody
import com.f0x1d.foxbin.network.model.response.FoxBinAuthResponse
import com.f0x1d.foxbin.network.model.response.FoxBinCreatedDocumentResponse
import com.f0x1d.foxbin.network.model.response.FoxBinNoteResponse
import com.f0x1d.foxbin.network.model.response.FoxBinNotesResponse
import retrofit2.http.*

interface FoxBinService {

    @POST("users/login")
    suspend fun login(@Body body: FoxBinAuthRequestBody): FoxBinAuthResponse

    @POST("users/register")
    suspend fun register(@Body body: FoxBinAuthRequestBody): FoxBinAuthResponse

    @GET("getAll")
    suspend fun getAll(@Query("accessToken") accessToken: String): FoxBinNotesResponse

    @GET("get/{slug}")
    suspend fun get(@Path("slug") slug: String, @Query("accessToken") accessToken: String?): FoxBinNoteResponse

    @GET("get/{slug}")
    suspend fun get(@Path("slug") slug: String): FoxBinNoteResponse

    @POST("create")
    suspend fun create(@Body body: FoxBinCreateDocumentRequestBody): FoxBinCreatedDocumentResponse

    @POST("edit")
    suspend fun edit(@Body body: FoxBinCreateDocumentRequestBody): FoxBinCreatedDocumentResponse

    @GET("delete/{slug}")
    suspend fun delete(@Path("slug") slug: String, @Query("accessToken") token: String)
}