package com.fang.taipeitour.ui.screen.home

import com.fang.taipeitour.model.attraction.Attraction
sealed class Item {
    data class Data(val attraction: Attraction) : Item()
    object Loading : Item()
}
