package com.module.taipeitourapi.external.model.response.common

/**
 * 相關圖片
 * @param src Ex: "https://www.travel.taipei/image/193316"
 * @param ext Ex: ".jpg"
 */
data class Image(
    val src: String,
    val ext: String,
)
