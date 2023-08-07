package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 相關圖片
 * @param src "https://www.travel.taipei/image/193316"
 * @param ext ".jpg"
 */
@Parcelize
data class Image(
    val src: String,
    val ext: String,
) : Parcelable
