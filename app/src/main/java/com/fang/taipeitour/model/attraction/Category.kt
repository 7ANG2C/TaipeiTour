package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 分類
 * @param id 分類編號
 * @param name 分類名稱
 */
@Parcelize
data class Category(
    val id: Int,
    val name: String,
) : Parcelable
