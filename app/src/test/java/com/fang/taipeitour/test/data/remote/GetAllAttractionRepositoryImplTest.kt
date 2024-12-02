package com.fang.taipeitour.test.data.remote

import com.fang.taipeitour.data.remote.GetAllAttractionRepository
import com.fang.taipeitour.data.remote.GetAllAttractionRepositoryImpl
import com.fang.taipeitour.model.language.Language
import com.module.taipeitourapi.external.data.TaipeiTourApi
import com.module.taipeitourapi.external.model.response.attration.Attraction
import com.module.taipeitourapi.external.model.response.attration.AttractionBundle
import com.module.taipeitourapi.external.model.response.common.Category
import com.module.taipeitourapi.external.model.response.common.Image
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.module.taipeitourapi.external.model.request.Language as ServiceLanguage

@ExperimentalCoroutinesApi
class GetAllAttractionRepositoryImplTest {
    private lateinit var repository: GetAllAttractionRepository
    private lateinit var api: TaipeiTourApi
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        api = mockk()
        repository = GetAllAttractionRepositoryImpl(api)
    }

    @Test
    fun testGetAllAttractionsSuccess() =
        runTest(testDispatcher) {
            val mockAttractions =
                listOf(
                    Attraction(
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
                        categories =
                            listOf(
                                Category(1, "Landmarks"),
                                Category(2, "Tourist Spots"),
                            ),
                        targets = listOf(Category(3, "Visitors"), Category(4, "Citizens")),
                        images = listOf(Image("https://example.com/image.jpg", "jpg")),
                    ),
                )
            val mockBundle = AttractionBundle(total = 1, attractions = mockAttractions)

            coEvery {
                api.getAllAttractions(ServiceLanguage.ENGLISH, 1).getOrThrow()
            } returns mockBundle

            val result = repository.invoke(1, Language.ENGLISH)
            assert(result.isSuccess)
        }
}
