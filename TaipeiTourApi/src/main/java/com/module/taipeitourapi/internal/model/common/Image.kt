package com.module.taipeitourapi.internal.model.common

import com.google.gson.annotations.SerializedName

/**
 * 相關圖片
 * @param src Ex: "https://www.travel.taipei/image/193316"
 * @param ext Ex: ".jpg"
 */
internal data class Image(
    @SerializedName("src")
    val src: String,
    @SerializedName("ext")
    val ext: String,
)
