package com.fang.taipeitour.service

import com.fang.taipeitour.service.model.Category
import com.fang.taipeitour.service.model.File
import com.fang.taipeitour.service.model.Image
import com.fang.taipeitour.service.model.Link
import com.google.gson.annotations.SerializedName

/**
 * @param size
 * @param data
 */
data class Attraction(
    @SerializedName("total")
    val size: Int,

    @SerializedName("data")
    val data: List<Data>
) {

    /**
     * @param id 2213
     * @param name "葫蘆國小跨堤人行陸橋"
     * @param nameZh null
     * @param open_status 1
     * @param introduction "位於葫蘆國小附近的跨堤人行陸橋，..."
     * @param open_time ""
     * @param zipcode "111"
     * @param distric "士林區"
     * @param address "111 臺北市士林區環河北路三段"
     * @param tel "+886-2-27208889"
     * @param fax ""
     * @param email ""
     * @param months "01,07,02,08,03,09,04,10,05,11,06,12"
     * @param nlat 25.08332
     * @param elong 121.5065
     * @param officialSite ""
     * @param facebook ""
     * @param ticket ""
     * @param remind "此處為行人及自行車通道，拍照時勿長時間停留阻擋用路人，並保持適當距離以策安全..."
     * @param stayTime ""
     * @param modified "2022-12-05 15:42:27 +08:00"
     * @param url "https://www.travel.taipei/zh-tw/attraction/details/2213"
     * @param categories
     * @param targets
     * @param services
     * @param friendly
     * @param images
     * @param files
     * @param links
     */
    data class Data(
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_zh")
        val nameZh: String,
        @SerializedName("open_status")
        val open_status: Int,
        @SerializedName("introduction")
        val introduction: String,
        @SerializedName("open_time") // "",
        val open_time: String,
        @SerializedName("zipcode")
        val zipcode: String,
        @SerializedName("distric")
        val distric: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("tel")
        val tel: String,
        @SerializedName("fax")
        val fax: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("months")
        val months: String,
        @SerializedName("nlat")
        val nlat: Double,
        @SerializedName("elong")
        val elong: Double,
        @SerializedName("official_site")
        val officialSite: String,
        @SerializedName("facebook")
        val facebook: String,
        @SerializedName("ticket")
        val ticket: String,
        @SerializedName("remind")
        val remind: String,
        @SerializedName("staytime")
        val stayTime: String,
        @SerializedName("modified")
        val modified: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("category")
        val categories: List<Category>,
        @SerializedName("target")
        val targets: List<Category>,
        @SerializedName("service")
        val services: List<Category>,
        @SerializedName("friendly")
        val friendly: List<Category>,
        @SerializedName("images")
        val images: List<Image>,
        @SerializedName("files")
        val files: List<File>,
        @SerializedName("links")
        val links: List<Link>,
    )

}