package com.module.taipeitourapi.test.internal.data

import com.module.taipeitourapi.external.data.RetrofitProvider
import com.module.taipeitourapi.external.model.request.Language
import com.module.taipeitourapi.internal.model.attration.Attraction
import com.module.taipeitourapi.internal.model.attration.AttractionBundle
import com.module.taipeitourapi.internal.model.common.Category
import com.module.taipeitourapi.internal.model.common.Image
import com.module.taipeitourapi.internal.TaipeiTourApiService
import com.module.taipeitourapi.internal.data.TaipeiTourApiImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TaipeiTourApiImplTest {

    private lateinit var api: TaipeiTourApiImpl
    private lateinit var retrofitProvider: RetrofitProvider
    private lateinit var service: TaipeiTourApiService

    @Before
    fun setUp() {
        retrofitProvider = mockk()
        service = mockk()
        api = TaipeiTourApiImpl(retrofitProvider)

        coEvery { retrofitProvider.invoke() } returns mockk()
        coEvery { retrofitProvider.invoke().create(TaipeiTourApiService::class.java) } returns service
    }

    @Test
    fun testGetAllAttractionsSuccess() = runBlocking {
        val expectedAttraction = Attraction(
            id = "1",
            name = "Taipei 101",
            introduction = "A famous landmark in Taipei.",
            zipCode = "100",
            distric = "Xinyi District",
            address = "No. 7, Section 5, Xinyi Road",
            northLatitude = 25.033611,
            eastLongitude = 121.565000,
            officialSite = "https://www.taipei-101.com.tw/",
            originalUrl = "https://en.wikipedia.org/wiki/Taipei_101",
            tel = "+886 2 8101 8000",
            fax = "+886 2 8101 9000",
            email = "info@taipei-101.com.tw",
            modified = "2023-08-01",
            categories = listOf(Category(1, "Landmarks"), Category(2, "Tourist Spots")),
            targets = listOf(Category(3, "Visitors"), Category(4, "Citizens")),
            images = listOf(Image("https://example.com/image.jpg", "jpg"))
        )
        val expectedBundle = AttractionBundle(total = 1, attractions = listOf(expectedAttraction))

        coEvery { service.getAllAttractions("en", 1) } returns expectedBundle

        val result = api.getAllAttractions(Language.ENGLISH, 1)
        assert( result.isSuccess)
    }

    @Test
    fun testGetAllAttractionsFailure() = runBlocking {
        coEvery { service.getAllAttractions("en", 1) } throws Exception("API error")

        val result = api.getAllAttractions(Language.ENGLISH, 1)
        assert( result.isFailure)
    }
}