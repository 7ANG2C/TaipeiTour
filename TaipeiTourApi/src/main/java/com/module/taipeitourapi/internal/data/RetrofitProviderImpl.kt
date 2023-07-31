package com.module.taipeitourapi.internal.data

import com.module.taipeitourapi.external.data.RetrofitProvider
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitProvider default implementation
 */
class RetrofitProviderImpl : RetrofitProvider {

    override operator fun invoke(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectionSpecs(
                listOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)
            )
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://www.travel.taipei/open-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}
