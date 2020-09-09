package com.appGarrage.search.di.modules

import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class AppGlideModule : AppGlideModule() {

    companion object {
        fun requestOptions(context: Context): RequestOptions {
            return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
    }
}