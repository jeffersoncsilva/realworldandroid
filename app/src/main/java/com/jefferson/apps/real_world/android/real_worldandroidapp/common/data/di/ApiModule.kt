package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.di

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiConstants
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.PetFinderApi
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors.AuthenticationInterceptor
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors.LoggingInterceptor
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder) : PetFinderApi{
        return builder.build().create(PetFinderApi::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideOkHttpClient(
        httpLogginINterceptos: HttpLoggingInterceptor,
        networkStatusInterceptor: NetworkStatusInterceptor,
        authenticationInterceptor: AuthenticationInterceptor
    ) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(httpLogginINterceptos)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(
        loggingInterceptor: LoggingInterceptor
    ): HttpLoggingInterceptor{
        val inteceptor = HttpLoggingInterceptor(loggingInterceptor)
        inteceptor.level = HttpLoggingInterceptor.Level.BODY
        return inteceptor
    }

}