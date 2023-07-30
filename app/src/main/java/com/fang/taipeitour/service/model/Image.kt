package com.fang.taipeitour.service.model

import com.google.gson.annotations.SerializedName

/**
 * 相關圖片
 * @param src "https://www.travel.taipei/image/193316"
 * @param subject ""
 * @param ext ".jpg"
 */
data class Image(
    @SerializedName("src")
    val src: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("ext")
    val ext: String,
)
