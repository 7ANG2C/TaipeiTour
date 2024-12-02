package com.fang.taipeitour.util

import android.content.Context
import android.location.LocationManager
import androidx.core.content.getSystemService

object LocationUtil {
    /**
     * 是否開啟定位
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService<LocationManager>()
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
    }
}
