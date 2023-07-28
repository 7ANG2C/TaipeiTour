package com.fang.taipeitour.data.attraction

import com.fang.taipeitour.service.Attraction
import com.fang.taipeitour.service.TaipeiTourApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttractionRepository() {

    suspend operator fun invoke(): Attraction {
        return withContext(Dispatchers.IO) {
            TaipeiTourApi().getAllAttractions("zh-tw")
        }
    }

}
