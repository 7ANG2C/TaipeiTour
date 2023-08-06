package com.fang.taipeitour.data.remote

import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.attraction.Category
import com.fang.taipeitour.model.attraction.Image
import com.fang.taipeitour.model.language.Language
import com.module.taipeitourapi.external.data.TaipeiTourApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.module.taipeitourapi.external.model.request.Language as ServiceLanguage

class GetAllAttractionRepositoryImpl(
    private val api: TaipeiTourApi
) : GetAllAttractionRepository {

    override suspend fun invoke(page: Int, language: Language): Result<List<Attraction>> {
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
        return withContext(Dispatchers.IO) {
            api.getAllAttractions(serviceLanguage, page)
        }.mapCatching { bundle ->
            bundle.attractions.map { attraction ->
                Attraction(
                    id = attraction.id,
                    name = attraction.name,
                    introduction = attraction.introduction,
                    zipCode = attraction.zipCode,
                    distric = attraction.distric,
                    address = attraction.address,
                    northLatitude = attraction.northLatitude,
                    eastLongitude = attraction.eastLongitude,
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
    }
}
