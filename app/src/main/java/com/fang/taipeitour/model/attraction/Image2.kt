package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 相關圖片
 * @param src "https://www.travel.taipei/image/193316"
 * @param subject ""
 * @param ext ".jpg"
 */
@Parcelize
data class Image2(
    val src: String,
    val subject: String,
    val ext: String,
) : Parcelable
