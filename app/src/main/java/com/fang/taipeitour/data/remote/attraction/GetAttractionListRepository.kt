package com.fang.taipeitour.data.remote.attraction

import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.coroutines.flow.Flow

interface GetAttractionListRepository {

    /**
     * 批次數量
     */
    val perSize: Int

    fun invoke(page: Int): Flow<List<Attraction>>

}
