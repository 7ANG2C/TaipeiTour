package com.module.taipeitourapi.internal.data

import com.module.taipeitourapi.external.data.RetrofitProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProviderImplTest {

    private lateinit var retrofitProvider: RetrofitProvider

    @Before
    fun setup() {
        retrofitProvider = RetrofitProviderImpl()
    }

    @Test
    fun testInvoke() {
        // Use MockK to mock the OkHttpClient.Builder
        val mockOkHttpClientBuilder: OkHttpClient.Builder = mockk()

        // Use MockK to mock the OkHttpClient
        val mockOkHttpClient: OkHttpClient = mockk()

        // Use MockK to mock the Retrofit.Builder
        val mockRetrofitBuilder: Retrofit.Builder = mockk()

        // Use MockK to mock the Retrofit
        val mockRetrofit: Retrofit = mockk()

        // Define the expected base URL
        val expectedBaseUrl = "https://www.travel.taipei/open-api/"

        // Define the expected read and connect timeout values
        val expectedReadTimeout = 60L
        val expectedConnectTimeout = 60L

        // Mock the behavior of OkHttpClient.Builder
        mockkStatic("okhttp3.OkHttpClient")
        coEvery { OkHttpClient.Builder() } returns mockOkHttpClientBuilder
        coEvery {
            mockOkHttpClientBuilder.connectionSpecs(any())
            mockOkHttpClientBuilder.connectTimeout(expectedConnectTimeout, TimeUnit.SECONDS)
            mockOkHttpClientBuilder.readTimeout(expectedReadTimeout, TimeUnit.SECONDS)
            mockOkHttpClientBuilder.build()
        } returns mockOkHttpClient

        // Mock the behavior of Retrofit.Builder
        coEvery { Retrofit.Builder() } returns mockRetrofitBuilder
        coEvery {
            mockRetrofitBuilder.baseUrl(expectedBaseUrl)
            mockRetrofitBuilder.addConverterFactory(any<GsonConverterFactory>())
            mockRetrofitBuilder.client(mockOkHttpClient)
            mockRetrofitBuilder.build()
        } returns mockRetrofit

        // Call the invoke method on the RetrofitProvider
        retrofitProvider.invoke()

        // Verify that the expected methods are called on OkHttpClient.Builder and Retrofit.Builder
        verify {
            mockOkHttpClientBuilder.connectionSpecs(any())
            mockOkHttpClientBuilder.connectTimeout(expectedConnectTimeout, TimeUnit.SECONDS)
            mockOkHttpClientBuilder.readTimeout(expectedReadTimeout, TimeUnit.SECONDS)
            mockRetrofitBuilder.baseUrl(expectedBaseUrl)
            mockRetrofitBuilder.addConverterFactory(any<GsonConverterFactory>())
            mockRetrofitBuilder.client(mockOkHttpClient)
            mockRetrofitBuilder.build()
        }
    }
}