package com.module.taipeitourapi.external.data

import com.module.taipeitourapi.external.model.request.Language
import com.module.taipeitourapi.external.model.response.attration.AttractionBundle

/**
 * TaipeiTourApi Provider
 */
interface TaipeiTourApi {

    /**
     * 批次數量
     */
    val perSize: Int

    /**
     * 取得所有熱門景點
     */
    suspend fun getAllAttractions(language: Language, page: Int): Result<AttractionBundle>
}
