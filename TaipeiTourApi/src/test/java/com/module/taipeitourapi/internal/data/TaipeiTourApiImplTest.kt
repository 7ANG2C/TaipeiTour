package com.module.taipeitourapi.internal.data

import android.util.Log
import com.module.taipeitourapi.external.data.RetrofitProvider
import com.module.taipeitourapi.external.data.TaipeiTourApi
import com.module.taipeitourapi.external.model.request.Language
import com.module.taipeitourapi.external.model.response.common.Category
import com.module.taipeitourapi.external.model.response.common.Image
import com.module.taipeitourapi.internal.model.attration.Attraction
import com.module.taipeitourapi.internal.model.attration.AttractionBundle
import com.module.taipeitourapi.internal.TaipeiTourApiService
import io.mockk.coEvery
import io.mockk.mockk
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class TaipeiTourApiImplTest {
//
//    private lateinit var taipeiTourApi: TaipeiTourApi
//
//    @Before
//    fun setup() {
//        // Mock the RetrofitProvider and inject it into TaipeiTourApiImpl
//        val mockRetrofitProvider = object : RetrofitProvider {
//            override fun invoke(): Retrofit {
//                val client = OkHttpClient.Builder()
//                    .connectionSpecs(
//                        listOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)
//                    )
//                    .connectTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .build()
//                return Retrofit.Builder()
//                    .baseUrl("https://www.travel.taipei/open-api/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
//                    .build()
//            }
//        }
//
//        taipeiTourApi = TaipeiTourApiImpl(mockRetrofitProvider)
//    }
//
//    @Test
//    fun testGetAllAttractions() = runBlocking {
//        // Mock the response from the API
//        val mockLanguage = Language.ENGLISH // Use the desired language here
//        val mockPage = 1 // Use the desired page number here
//        val mockAttractions = emptyList<Attraction>()
//        val mockTotal = 100 // Use the desired total count here
//
//        val expectedBundle = AttractionBundle(
//            total = mockTotal,
//            attractions = mockAttractions
//        )
//
//        // Use the mock response to override the actual API call
//        taipeiTourApi.getAllAttractions(mockLanguage, mockPage)
//
//        // Perform the actual method call
//        val result = taipeiTourApi.getAllAttractions(mockLanguage, mockPage)
//
//        // Assertions
//        assertEquals(Result.success(expectedBundle).isSuccess, result.isSuccess)
//    }

    private lateinit var taipeiTourApi: TaipeiTourApi
    private val mockRetrofitProvider: RetrofitProvider = mockk()
    private val retrofit: Retrofit = mockk<Retrofit>()

    @Before
    fun setup() {
        taipeiTourApi = TaipeiTourApiImpl(mockRetrofitProvider)
    }

    @Test
    fun testGetAllAttractions() = runBlocking {
        // Mock the response from the API
        val mockLanguage = Language.ENGLISH // Use the desired language here
        val mockPage = 1 // Use the desired page number here
        val mockAttractions = emptyList<Attraction>()
        val mockTotal = 100 // Use the desired total count here

        val expectedBundle = AttractionBundle(
            total = mockTotal,
            attractions = mockAttractions
        )

        // Use MockK to mock the RetrofitProvider behavior
        coEvery { mockRetrofitProvider.invoke() } returns createMockRetrofit()

        // Perform the actual method call
        val result = taipeiTourApi.getAllAttractions(mockLanguage, mockPage)

//        Log.d("werwrwer", result.toString())
        print(result.toString())
        // Assertions
        assert(result.isSuccess)
//        assertEquals(Result.success(expectedBundle).isSuccess, result.isSuccess)
    }

    private fun createMockRetrofit(): Retrofit {
        return Retrofit.Builder()
                    .baseUrl("https://www.travel.taipei/open-api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
    }
}