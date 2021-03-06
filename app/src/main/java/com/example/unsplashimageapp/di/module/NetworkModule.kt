package com.example.unsplashimageapp.di.module


import com.example.unsplashimageapp.BuildConfig
import com.example.unsplashimageapp.data.api.UnSplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule
{


    @Singleton
    @Provides
    fun providesIntercepton() : Interceptor =
        object :Interceptor
        {
            override fun intercept(chain: Interceptor.Chain): Response =
                chain.run{
                    proceed(
                        request()
                        .newBuilder()
                            .addHeader("Accept-Version","v1")
                            .addHeader("Authorization","Client-ID XrgZaFt9EF1Fnn5j4kRwtKyl8VwSpkHf2_Abo0PBSRA")
                            .build()
                    )
                }
        }

    @Singleton
    @Provides
    fun providesOkhttpClient(interceptor: Interceptor) : OkHttpClient
    = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30,TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()




    @Singleton
    @Provides
    fun provideRetrofitClient (okHttpClient: OkHttpClient) : Retrofit
    = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideUnSplashApi(retrofit: Retrofit) : UnSplashApi = retrofit.create(UnSplashApi::class.java)


}