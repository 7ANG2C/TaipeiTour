package com.module.taipeitourapi.external.data

import com.module.taipeitourapi.internal.TaipeiTourApiService
import retrofit2.Retrofit

/**
 * Provider retrofit for create [TaipeiTourApiService]
 */
interface RetrofitProvider {
    operator fun invoke(): Retrofit
}
