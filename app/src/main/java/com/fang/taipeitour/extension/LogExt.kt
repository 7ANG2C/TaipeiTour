package com.fang.taipeitour.extension

import android.util.Log
import com.fang.taipeitour.BuildConfig

fun logD(tag: String, vararg any: Any?) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, any.joinToString { it?.toString() ?: "NULL" })
    }
}
