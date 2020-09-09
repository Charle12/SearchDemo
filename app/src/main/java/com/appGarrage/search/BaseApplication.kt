package com.appGarrage.search

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.appGarrage.search.di.modules.appModule
import com.appGarrage.search.di.modules.networkModule
import com.appGarrage.search.helper.SharedPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level


class BaseApplication : MultiDexApplication() , LifecycleObserver {

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext() : BaseApplication {
            return instance as BaseApplication
        }

    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        createModules()
        SharedPrefs.initSharedPrefs(this)
    }

     fun createModules()
    {
        stopKoin()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(appModule, networkModule))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("appStatus", "App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("appStatus", "App in foreground")
    }

}