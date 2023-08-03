package com.fang.taipeitour.data.remote.attraction

import com.fang.taipeitour.data.local.UserPreferencesRepository
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.attraction.Category
import com.fang.taipeitour.model.attraction.Image
import com.fang.taipeitour.model.language.Language
import com.module.taipeitourapi.external.data.TaipeiTourApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import com.module.taipeitourapi.external.model.request.Language as ServiceLanguage

/**
 * 取得所有景點 repository implementation
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetAttractionListRepositoryImpl(
    private val api: TaipeiTourApi,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetAttractionListRepository {

    override val dataSizePerPage = 30

    override fun invoke(page: Int) = userPreferencesRepository.getLanguage()
        .mapLatest { language ->
            val serviceLanguage = when (language) {
                Language.TAIWAN -> ServiceLanguage.TAIWAN
                Language.CHINA -> ServiceLanguage.CHINA
                Language.ENGLISH -> ServiceLanguage.ENGLISH
                Language.JAPAN -> ServiceLanguage.JAPAN
                Language.KOREA -> ServiceLanguage.KOREA
                Language.SPAN -> ServiceLanguage.SPAN
                Language.INDONESIA -> ServiceLanguage.INDONESIA
                Language.THAILAND -> ServiceLanguage.THAILAND
                Language.VIETNAM -> ServiceLanguage.VIETNAM
            }
            val result = api.getAllAttractions(serviceLanguage, page)

            result.getOrThrow().attractions.map { attraction ->
                Attraction(
                    id = attraction.id,
                    name = attraction.name,
                    introduction = attraction.introduction,
                    zipCode = attraction.zipCode,
                    distric = attraction.distric,
                    address = attraction.address,
                    officialSite = attraction.officialSite,
                    originalUrl = attraction.originalUrl,
                    tel = attraction.tel,
                    fax = attraction.fax,
                    email = attraction.email,
                    modified = attraction.modified,
                    categories = attraction.categories.map {
                        Category(it.id, it.name)
                    },
                    targets = attraction.targets.map {
                        Category(it.id, it.name)
                    },
                    images = attraction.images.map {
                        Image(it.src, it.ext)
                    },
                )
            }
        }
        .flowOn(Dispatchers.IO)
}
