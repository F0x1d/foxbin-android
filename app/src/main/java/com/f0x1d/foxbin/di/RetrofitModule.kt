package com.f0x1d.foxbin.di

import com.f0x1d.foxbin.FoxBinApp
import com.f0x1d.foxbin.network.service.FoxBinService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideFoxBinService() = Retrofit.Builder()
        .baseUrl(FoxBinApp.FOXBIN_DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoxBinService::class.java)
}