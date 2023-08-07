package com.fang.taipeitour.data.remote

import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.Language

interface GetAllAttractionRepository {

    /**
     * 取得熱門景點
     * @param language 語系代碼
     * @param page 頁碼 (每次回應30筆資料)
     */
    suspend fun invoke(page: Int, language: Language): Result<List<Attraction>>
}
