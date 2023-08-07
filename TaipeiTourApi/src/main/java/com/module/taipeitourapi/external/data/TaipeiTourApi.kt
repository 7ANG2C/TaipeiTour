package com.module.taipeitourapi.external.data

import com.module.taipeitourapi.external.model.request.Language
import com.module.taipeitourapi.external.model.response.attration.AttractionBundle

/**
 * TaipeiTourApi Provider
 */
interface TaipeiTourApi {

    /**
     * 取得熱門景點
     * @param language 語系代碼
     * @param page 頁碼 (每次回應30筆資料)
     */
    suspend fun getAllAttractions(language: Language, page: Int): Result<AttractionBundle>
}
