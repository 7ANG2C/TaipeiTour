package com.module.taipeitourapi.external.model.response.attration

import com.module.taipeitourapi.external.model.response.common.Category
import com.module.taipeitourapi.external.model.response.common.Image

/**
 * 熱門景點
 * @param id 編號 Ex: 2213
 * @param name 標題 Ex: "葫蘆國小跨堤人行陸橋"
 * @param introduction 介紹 Ex: "位於葫蘆國小附近的跨堤人行陸橋，..."
 * @param zipCode 郵遞區號 Ex: "111"
 * @param distric 行政區 Ex: "士林區"
 * @param address 地址 Ex: "111 臺北市士林區環河北路三段"
 * @param officialSite 官方網站網址 Ex: "https://hotspringmuseum.taipei"
 * @param originalUrl 資料源網址 Ex: "https://www.travel.taipei/zh-tw/attraction/details/2213"
 * @param tel 電話 Ex: "+886-2-27208889"
 * @param fax 傳真 Ex: "+886-2-28852885"
 * @param email 電子郵件 Ex: "grand@grand-hotel.org"
 * @param modified Ex: 最後修改時間 "2022-12-05 15:42:27 +08:00"
 * @param categories 主題分類
 * @param targets 對象
 * @param images 相關圖片
 */
data class Attraction(
    val id: String,
    val name: String,
    val introduction: String,
    val zipCode: String,
    val distric: String,
    val address: String,
    val officialSite: String,
    val originalUrl: String,
    val tel: String,
    val fax: String,
    val email: String,
    val modified: String,
    val categories: List<Category>,
    val targets: List<Category>,
    val images: List<Image>,
)
