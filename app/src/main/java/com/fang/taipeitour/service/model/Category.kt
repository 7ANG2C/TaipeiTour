package com.fang.taipeitour.service.model

import com.google.gson.annotations.SerializedName

/**
 * 分類
 * @param id 分類編號
 * @param name 分類名稱
 */
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)