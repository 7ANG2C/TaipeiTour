package com.fang.taipeitour.model.attraction

import android.os.Parcelable
import com.fang.taipeitour.service.Attraction
import kotlinx.parcelize.Parcelize

/**
 * @param id 2213
 * @param name "葫蘆國小跨堤人行陸橋"
 * @param nameZh null
 * @param open_status 1
 * @param introduction "位於葫蘆國小附近的跨堤人行陸橋，..."
 * @param openTime ""
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
@Parcelize
data class Attraction2(
    val id: String,
    val name: String,
    val nameZh: String?,
    val openStatus: Int,
    val introduction: String,
    val openTime: String,
    val zipcode: String,
    val distric: String,
    val address: String,
    val tel: String,
    val fax: String,
    val email: String,
    val months: String,
    val nlat: Double,
    val elong: Double,
    val officialSite: String,
    val facebook: String,
    val ticket: String,
    val remind: String,
    val stayTime: String,
    val modified: String,
    val url: String,
    val categories: List<Category2>,
    val targets: List<Category2>,
    val services: List<Category2>,
    val friendly: List<Category2>,
    val images: List<Image2>,
    val files: List<File2>,
    val links: List<Link2>,
) : Parcelable {

}

fun convert(it: Attraction.Data): Attraction2 {
    return Attraction2(
        id = it.id,
        name = it.name,
        nameZh = it.nameZh,
        openStatus = it.openStatus,
        introduction = it.introduction,
        openTime = it.openTime,
        zipcode = it.zipcode,
        distric = it.distric,
        address = it.address,
        tel = it.tel,
        fax = it.fax,
        email = it.email,
        months = it.months,
        nlat = it.nlat,
        elong = it.elong,
        officialSite = it.officialSite,
        facebook = it.facebook,
        ticket = it.ticket,
        remind = it.remind,
        stayTime = it.stayTime,
        modified = it.modified,
        url = it.url,
        categories = it.categories.map { Category2(it.id, it.name) },
        targets = it.targets.map { Category2(it.id, it.name) },
        services = it.services.map { Category2(it.id, it.name) },
        friendly = it.friendly.map { Category2(it.id, it.name) },
        images = it.images.map { Image2(it.src, it.subject, it.ext) },
        files = it.files.map { File2(it.src, it.subject, it.ext) },
        links = it.links.map { Link2(it.src, it.subject) },
    )
}

