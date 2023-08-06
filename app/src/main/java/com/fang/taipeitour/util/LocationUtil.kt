package com.fang.taipeitour.util

import android.content.Context
import android.location.LocationManager
import androidx.core.content.getSystemService

object LocationUtil {
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService<LocationManager>()
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
    }
}
