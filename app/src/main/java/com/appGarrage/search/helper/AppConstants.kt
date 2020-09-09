package com.appGarrage.search.helper
import java.io.File

class AppConstants {
    companion object{

        var AUTH_TOKEN: String = ""
        var USER_NMAE: String = ""
        var USER_NUMBER: String = ""

        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        val UTM_SOURCE: String = "Android"
        var DEVICE_UDID: String = ""
        var RANDOM_NUMBER: String = ""
        var UPDATED_CART_QUANTITY: Int = 0

        var COMPRESSED_IMAGE_WIDTH = 1450
        var COMPRESSED_IMAGE_HEIGHT = 900
        var COMPRESSED_IMAGE_QUALITY = 90

        val KEY_EMAIL: String = "EMAIL_ID"
        val KEY_TEMP_TOKEN: String = "TEMP_TOEKN"
        val KEY_MESSAGE: String = "MESSAGE"
        var PRESCRIPTION_IMAGE: File? = null
    }
}

