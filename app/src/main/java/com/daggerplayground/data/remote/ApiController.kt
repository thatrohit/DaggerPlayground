package com.daggerplayground.data.remote

import com.daggerplayground.BuildConfig
import com.google.gson.GsonBuilder
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
object ApiController {

    /**
     * returns an okhttp client with content-type set in header
     */
    private var okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder().apply {
                header("Content-Type", "application/json")
            }
            val request = requestBuilder.method(original.method(), original.body()).build()
            return@addInterceptor it.proceed(request)
        }.build()

    /**
     * returns retrofit instance for an api interface and a base url
     * with rx adapter factory
     */
    private inline fun <reified T> createWebService(
        okHttpClient: OkHttpClient, baseUrl: String
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(T::class.java)
    }

    @Provides
    @JvmStatic
    fun getRetrofit(): ApiInterface = createWebService(okHttpClient, BuildConfig.BASE_URL)
}

@Component(modules = [ApiController::class])
interface ApiFactory {
    fun api(): ApiInterface
}