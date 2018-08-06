package com.afflyas.fwcwallpapers.di

import com.afflyas.fwcwallpapers.api.PixabayApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module to provide single Retrofit instance and ViewModel's modules
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    /**
     *
     * Provide single retrofit instance of [PixabayApiService]
     *
     * @return instance of [PixabayApiService]
     */
    @Singleton
    @Provides
    fun provideApiService(): PixabayApiService {
        return Retrofit.Builder()
                .baseUrl(PixabayApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PixabayApiService::class.java)
    }

}