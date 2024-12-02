package com.module.taipeitourapi.external.model.response.attration

/**
 * 熱門景點 bundle
 * @param total 總景點數量
 * @param attractions 當前頁碼景點 list
 */
data class AttractionBundle(
    val total: Int,
    val attractions: List<Attraction>,
)
