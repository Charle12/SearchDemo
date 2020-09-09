package com.appGarrage.search.di.modules

import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.appGarrage.search.BaseApplication
import com.appGarrage.search.BuildConfig
import com.appGarrage.search.data.remote.ApiService
import com.appGarrage.search.helper.AppConstants
import com.appGarrage.search.helper.log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

var networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideApplicationApi(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    log("provideRetrofit called =====${AppConstants.AUTH_TOKEN}")
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    log("provideOkHttpClient called +++++++${AppConstants.AUTH_TOKEN}")
    log("provideOkHttpClient called +++++++${AppConstants.AUTH_TOKEN}")
    Log.d("provideOkHttpClient", "token:" +AppConstants.AUTH_TOKEN)
    if (AppConstants.AUTH_TOKEN.isEmpty()) {
        return OkHttpClient().newBuilder()
            .addInterceptor(ChuckerInterceptor(BaseApplication.applicationContext()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    } else {
        return OkHttpClient().newBuilder()
            .addInterceptor(OAuthInterceptor("Bearer", AppConstants.AUTH_TOKEN))
            .addInterceptor(ChuckerInterceptor(BaseApplication.applicationContext()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }
}

fun provideApplicationApi(retrofit: Retrofit): ApiService {
    log("provideApplicationApi called -------${AppConstants.AUTH_TOKEN}")
    return retrofit.create(ApiService::class.java)
}

