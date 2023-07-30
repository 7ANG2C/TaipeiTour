package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 相關附件
 * @param src 附件位址
 * @param subject 主旨
 * @param ext 格式
 */
@Parcelize
data class File2(
    val src: String,
    val subject: String,
    val ext: String,
) : Parcelable
