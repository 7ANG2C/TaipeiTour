package com.fang.taipeitour.data.remote

import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.Language
import kotlinx.coroutines.flow.Flow

interface GetAllAttractionRepository {
    suspend fun invoke(page: Int, language: Language): Result<List<Attraction>>
}
