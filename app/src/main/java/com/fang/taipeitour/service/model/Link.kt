package com.fang.taipeitour.service.model

import com.google.gson.annotations.SerializedName

/**
 * 相關連結
 * @param src 連結位址
 * @param subject 主旨
 */
data class Link(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String
)
