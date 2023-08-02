package com.module.taipeitourapi.internal

import com.module.taipeitourapi.internal.model.attration.AttractionBundle
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 臺北旅遊網 Open Api
 * @see <a href="https://www.travel.taipei/open-api/swagger/ui/index#/Attractions/Attractions_All">Swagger</a>
 */
internal interface TaipeiTourApiService {

    /**
     * 取得熱門景點
     * @param language 語系代碼
     * @param page 頁碼 (每次回應30筆資料)
     * @param categoryIds 查詢的分類編號 (可輸入多個請以逗號,分隔)。例如 12,34,124
     * @param northLatitude 查詢附近景點，經緯度(北緯) WGS84
     * @param eastLongitude 查詢附近景點，經緯度(東經) WGS84
     */
    @Headers("Accept: application/json")
    @GET("{lang}/Attractions/All")
    suspend fun getAllAttractions(
        @Path("lang") language: String,
        @Query("page") page: Int,
        @Query("categoryIds") categoryIds: String = "",
        @Query("nlat") northLatitude: String = "",
        @Query("elong") eastLongitude: String = "",
    ): AttractionBundle
}
