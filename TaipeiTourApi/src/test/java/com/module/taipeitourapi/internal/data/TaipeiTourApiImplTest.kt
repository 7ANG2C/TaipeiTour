package com.module.taipeitourapi.internal.data

import com.module.taipeitourapi.external.model.request.Language
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * TaipeiTourApi Test
 */
class TaipeiTourApiImplTest {

    private var taipeiTourApi: TaipeiTourApiImpl? = null
//    private val mockWebServer = MockWebServer()

    val perSize = 30

    @Test
    fun getAllAttractions(
        language: Language,
        page: Int
    ) {
        val result = runBlocking {
            taipeiTourApi?.getAllAttractions(Language.CHINA, 1)
        }
//        Truth.assertThat(result?.isEmpty()).isTrue()
//        Result.failure(IllegalStateException())
    }
}
