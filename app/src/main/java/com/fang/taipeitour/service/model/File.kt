package com.fang.taipeitour.service.model

import com.google.gson.annotations.SerializedName

/**
 * 相關附件
 * @param src 附件位址
 * @param subject 主旨
 * @param ext 格式
 */
data class File(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("ext")
    val ext: String,
)
