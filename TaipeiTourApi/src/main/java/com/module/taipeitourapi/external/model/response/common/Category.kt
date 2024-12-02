package com.module.taipeitourapi.external.model.response.common

/**
 * 分類
 * @param id 分類編號 Ex: 13
 * @param name 分類名稱 Ex: "歷史建築"
 */
data class Category(
    val id: Int,
    val name: String,
)
