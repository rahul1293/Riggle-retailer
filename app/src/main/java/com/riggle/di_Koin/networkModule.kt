package com.riggle.di_Koin


import com.google.gson.GsonBuilder
import com.riggle.BuildConfig
import com.riggle.data.models.response.ValidatorAdapterFactory
import com.riggle.data.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    factory { AuthInterceptor(get()) }
    factory { provideOkHttpClient(get(),get()) }
    factory { provideLoggingInterceptorV2() }
    factory { provideApiCaller(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    var gson = GsonBuilder().registerTypeAdapterFactory(ValidatorAdapterFactory()).create()
    var gsonOne = GsonBuilder().setLenient().create()
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonOne)).build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor).readTimeout(2,TimeUnit.MINUTES).writeTimeout(2,TimeUnit.MINUTES).build()
}

fun provideApiCaller(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

fun provideLoggingInterceptorV2(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        logging.setLevel(HttpLoggingInterceptor.Level.NONE)
    }
    return logging
}