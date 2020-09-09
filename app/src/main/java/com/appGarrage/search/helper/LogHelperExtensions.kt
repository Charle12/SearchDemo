package com.appGarrage.search.helper

import android.app.Activity
import android.util.Log

fun Activity.logd(message: String) {
     Log.d(this.javaClass.simpleName, message)
}

inline fun <reified T> logd(message: String) {
    Log.d(T::class.java.simpleName, message)
}

fun log(message: String) {
    Log.d("NON_ACTIVITY_LOGS", message)
}
