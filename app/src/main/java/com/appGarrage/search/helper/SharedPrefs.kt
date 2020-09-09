package com.appGarrage.search.helper

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    private lateinit var mSharedPref: SharedPreferences

    private const val PREFS_NAME = "params"

    const val AUTH_TOKEN = "auth_token"
    const val USER_EMAIL = "user_email"
    const val USER_NUMBER = "user_number"
    const val IS_SKIPPED = "is_skipped"
    const val IS_LOCATION_SET = "is_location_set"
    const val IS_GET_STARTED = "is_get_started"
    const val ID_USER = "id_user"
    const val CART_COUNT = "cart_count"
    const val DEVICE_UDID = "device_udid"
    const val RANDOM_NUMBER = "random_number"
    const val CURRENT_LOCATION = "current_location"
    const val FCM_TOKEN = "fcm_token"

    fun initSharedPrefs(context: Context) {
        mSharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String?): String? {
        return mSharedPref.getString(key, value)
    }

    fun read(key: String, value: Int): Int? {
        return mSharedPref.getInt(key, value)
    }

    fun read(key: String, value: Boolean): Boolean? {
        return mSharedPref.getBoolean(key, value)
    }

    fun read(key: String, value: Float): Float? {
        return mSharedPref.getFloat(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = mSharedPref.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun write(key: String, value: Int) {
        val prefsEditor: SharedPreferences.Editor = mSharedPref.edit()
        with(prefsEditor) {
            putInt(key, value)
            commit()
        }
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = mSharedPref.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            commit()
        }
    }

    fun write(key: String, value: Float) {
        val prefsEditor: SharedPreferences.Editor = mSharedPref.edit()
        with(prefsEditor) {
            putFloat(key, value)
            commit()
        }
    }


    fun clear() {
        val prefsEditor: SharedPreferences.Editor = mSharedPref.edit()
        prefsEditor.clear()
        prefsEditor.apply()
    }
}