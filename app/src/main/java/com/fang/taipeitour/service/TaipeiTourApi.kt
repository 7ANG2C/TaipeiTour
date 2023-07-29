package com.fang.taipeitour.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaipeiTourApi {
    private companion object {
        const val HOST = "https://www.travel.taipei/open-api/"
    }

    private val taipeiTourApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(TaipeiTourApiService::class.java)
    }

    suspend fun getAllAttractions(language: String, page: Int) =
        taipeiTourApiService.getAllAttractions(language, page)

}