package com.module.taipeitourapi.internal.model.attration

import com.google.gson.annotations.SerializedName

/**
 * 熱門景點 bundle
 * @param total 總景點數量
 * @param attractions 當前頁碼景點 list
 */
internal data class AttractionBundle(
    @SerializedName("total")
    val total: Int,
    @SerializedName("data")
    val attractions: List<Attraction>
)
