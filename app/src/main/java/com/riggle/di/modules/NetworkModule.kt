package com.riggle.di.modules
/*

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.riggle.BuildConfig
import com.riggle.data.network.ApiInterface
import com.riggle.di.scopes.ApplicationScope
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//@Module
class NetworkModule {
    //@Provides
    //@ApplicationScope
    fun provideOkHttpClientV2(
        logging: HttpLoggingInterceptor?,
        headerInterceptor: Interceptor?
    ): OkHttpClient {
        val spec: ConnectionSpec = Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
            )
            .build()

//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectionSpecs(Collections.singletonList(spec))
//                .build();
        val builder: Builder = Builder().addNetworkInterceptor(StethoInterceptor())
        return builder.addInterceptor(headerInterceptor)
            .addInterceptor(logging)
            .retryOnConnectionFailure(true)
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(
                100,
                TimeUnit.SECONDS
            ) //                .connectionSpecs(Arrays.asList(spec, ConnectionSpec.CLEARTEXT))
            .build()

        */
/*return builder.addInterceptor(headerInterceptor)
                .addInterceptor(logging)
                .retryOnConnectionFailure(true)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor()).build();*//*

    }

    // @Provides
    //  @ApplicationScope
    */
/*Interceptor provideRetrofitHeaderV2(final SharedPreferencesUtil sharedPreferencesUtil) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder builder = original.newBuilder();
                if (original.url().host().contains("api.riggleapp.in")) {
                    builder.header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    if (UserProfileSingleton.getInstance(RiggleApplication.Companion.getInstance()).isLogin()) {
                        builder.removeHeader("Authorization");
                        String loginToken = UserProfileSingleton.getInstance(RiggleApplication.Companion.getInstance()).getBearerToken();
                        builder.addHeader("Authorization", "Bearer " + loginToken);
                    }
                    builder.addHeader("Connection", "close");
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }*//*

    //@Provides
    //@ApplicationScope
    fun provideLoggingInterceptorV2(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return logging
    }

    //@Provides
    //@ApplicationScope
    fun provideRetrofitV2(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    fun getCommonApis(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}*/
