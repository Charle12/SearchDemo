package com.appGarrage.search.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.appGarrage.search.R
import com.appGarrage.search.databinding.ActivitySplashBinding
import com.appGarrage.search.helper.AppConstants
import com.appGarrage.search.helper.SharedPrefs
import com.appGarrage.search.helper.log
import com.shipmedtechnologies.onetab.ui.searchResult.SearchResultActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    /**
     * Load file user data
     */
    private val loadModelParameters: Map<String, Any> = HashMap()

    private lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        val animFadeIn: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        splashBinding.logoIv.startAnimation(animFadeIn)
        loadUserDetails()
    }

    private fun loadUserDetails() {
        AppConstants.AUTH_TOKEN = SharedPrefs.read(SharedPrefs.AUTH_TOKEN, "") ?: ""
        log("AUTH TOKEN :::::${AppConstants.AUTH_TOKEN}")
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        Handler().postDelayed(
            {
                if (AppConstants.AUTH_TOKEN.isNotEmpty()) {
//                    gotoHome()
                } else {
                    gotoImageDashboard()
                    //loadModelFromAssets()
                }
            },
            2000
        )
    }

    private fun gotoImageDashboard() {
        val intent = Intent(this, SearchResultActivity::class.java)
        startActivity(intent)
        finish()
    }
}