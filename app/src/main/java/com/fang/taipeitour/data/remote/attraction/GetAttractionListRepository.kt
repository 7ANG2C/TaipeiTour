package com.fang.taipeitour.data.remote.attraction

import com.fang.taipeitour.model.attraction.Attraction
import kotlinx.coroutines.flow.Flow

/**
 * 取得所有景點 repository
 */
interface GetAttractionListRepository {

    /**
     * 批次數量
     */
    val perSize: Int

    fun invoke(page: Int): Flow<List<Attraction>>
}
