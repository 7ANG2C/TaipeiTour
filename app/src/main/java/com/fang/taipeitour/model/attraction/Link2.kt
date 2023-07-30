package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 相關連結
 * @param src 連結位址
 * @param subject 主旨
 */
@Parcelize
data class Link2(
    val src: String,
    val subject: String
) : Parcelable

