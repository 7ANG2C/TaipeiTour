package com.fang.taipeitour.service

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @see <a href="https://www.travel.taipei/open-api/swagger/ui/index#/Attractions/Attractions_All">Swagger</a>
 */
interface TaipeiTourApiService {

    /**
     * @param lang
     * @param page
     * @param categoryIds
     * @param nlat
     * @param elong
     */
    @Headers("Accept: application/json")
    @GET("{lang}/Attractions/All")
    suspend fun getAllAttractions(
        @Path("lang") language: String,
        @Query("page") page: Int,
        @Query("categoryIds") categoryIds: String = "",
        @Query("nlat") nlat: String = "",
        @Query("elong") elong: String = "",
    ): Attraction

}
