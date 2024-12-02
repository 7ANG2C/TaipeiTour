package com.module.taipeitourapi.internal.data

import com.module.taipeitourapi.external.data.RetrofitProvider
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitProvider default implementation
 */
class RetrofitProviderImpl : RetrofitProvider {
    override operator fun invoke(): Retrofit {
        val client =
            OkHttpClient.Builder()
                .connectionSpecs(
                    listOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT),
                )
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
        return Retrofit.Builder()
            .baseUrl("https://www.travel.taipei/open-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
