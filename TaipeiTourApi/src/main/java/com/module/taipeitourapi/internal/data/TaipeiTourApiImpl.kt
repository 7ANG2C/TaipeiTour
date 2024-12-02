package com.module.taipeitourapi.internal.data

import com.module.taipeitourapi.external.data.RetrofitProvider
import com.module.taipeitourapi.external.data.TaipeiTourApi
import com.module.taipeitourapi.external.model.request.Language
import com.module.taipeitourapi.external.model.response.attration.Attraction
import com.module.taipeitourapi.external.model.response.attration.AttractionBundle
import com.module.taipeitourapi.external.model.response.common.Category
import com.module.taipeitourapi.external.model.response.common.Image
import com.module.taipeitourapi.internal.TaipeiTourApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * TaipeiTourApi default implementation
 */
class TaipeiTourApiImpl(private val retrofitProvider: RetrofitProvider) : TaipeiTourApi {
    private val service by lazy {
        retrofitProvider.invoke().create(TaipeiTourApiService::class.java)
    }

    /**
     * 取得熱門景點
     * @param language 語系代碼
     * @param page 頁碼 (每次回應30筆資料)
     */
    override suspend fun getAllAttractions(
        language: Language,
        page: Int,
    ): Result<AttractionBundle> {
        return withContext(Dispatchers.Default) {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    service.getAllAttractions(language.requestCode, page)
                }
            }
                .mapCatching { bundle ->
                    val attractions =
                        bundle.attractions.map { attraction ->
                            Attraction(
                                id = attraction.id.orEmpty(),
                                name = attraction.name.orEmpty(),
                                introduction = attraction.introduction.orEmpty(),
                                zipCode = attraction.zipCode.orEmpty(),
                                distric = attraction.distric.orEmpty(),
                                address = attraction.address.orEmpty(),
                                northLatitude = attraction.northLatitude,
                                eastLongitude = attraction.eastLongitude,
                                officialSite = attraction.officialSite.orEmpty(),
                                originalUrl = attraction.originalUrl.orEmpty(),
                                tel = attraction.tel.orEmpty(),
                                fax = attraction.fax.orEmpty(),
                                email = attraction.email.orEmpty(),
                                modified = attraction.modified.orEmpty(),
                                categories =
                                    attraction.categories.map {
                                        Category(it.id, it.name)
                                    },
                                targets =
                                    attraction.targets.map {
                                        Category(it.id, it.name)
                                    },
                                images =
                                    attraction.images.map {
                                        Image(it.src, it.ext)
                                    },
                            )
                        }
                    AttractionBundle(
                        total = bundle.total,
                        attractions = attractions,
                    )
                }
        }
    }
}
