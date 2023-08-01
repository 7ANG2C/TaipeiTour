package com.fang.taipeitour.ui.component

import androidx.annotation.DrawableRes
import com.fang.taipeitour.R

val noImageHolderRes
    @DrawableRes get() = listOf(
        R.drawable.no_image_holder1,
        R.drawable.no_image_holder2,
        R.drawable.no_image_holder3,
        R.drawable.no_image_holder4,
        R.drawable.no_image_holder5,
        R.drawable.no_image_holder6,
        R.drawable.no_image_holder7,
        R.drawable.no_image_holder8,
        R.drawable.no_image_holder9,
    ).random()