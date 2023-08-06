package com.fang.taipeitour.ui.screen.home.attraction

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttractionArgument(
    val attraction: Attraction,
    @DrawableRes val noImageHolderRes: Int
) : Parcelable
