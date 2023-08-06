package com.module.taipeitourapi.test.internal.data

import com.module.taipeitourapi.internal.TaipeiTourApiService
import com.module.taipeitourapi.internal.data.RetrofitProviderImpl
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class RetrofitProviderImplTest {

    private lateinit var retrofitProviderImpl: RetrofitProviderImpl

    @Before
    fun setUp() {
        retrofitProviderImpl = RetrofitProviderImpl()
    }

    @Test
    fun testRetrofitCreation() {
        val retrofit = retrofitProviderImpl.invoke()
        assertNotNull(retrofit)

        val service = retrofit.create(TaipeiTourApiService::class.java)
        assertNotNull(service)
    }

    @Test
    fun testRetrofitBaseUrl() {
        val retrofit = retrofitProviderImpl.invoke()

        val baseUrl = retrofit.baseUrl().toString()
        val expectedBaseUrl = "https://www.travel.taipei/open-api/"
        assertEquals(expectedBaseUrl, baseUrl)
    }

}