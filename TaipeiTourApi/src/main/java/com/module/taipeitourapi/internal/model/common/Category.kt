package com.module.taipeitourapi.internal.model.common

import com.google.gson.annotations.SerializedName

/**
 * 分類
 * @param id 分類編號 Ex: 13
 * @param name 分類名稱 Ex: "歷史建築"
 */
internal data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
