package com.appGarrage.search.helper

import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.appGarrage.search.di.modules.GlideApp
import java.io.File
import android.graphics.PorterDuff
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

// This extension allow to call a function directly on the View to load an image from network.
fun ImageView.loadNetworkImage(imageUrl: String?) {
    GlideApp.with(this)
        .load(imageUrl)
        //.placeholder(R.drawable.placeholder_square)
        .fitCenter()
        .into(this)
}

fun ImageView.loadFileImage(imageUrl: File?) {
    GlideApp.with(this)
        .load(imageUrl)
       // .placeholder(R.drawable.placeholder_square)
        .fitCenter()
        .into(this)
}

fun ImageView.loadBannerImage(imageUrl: String?) {
    GlideApp.with(this)
        .load(imageUrl)
        //.placeholder(R.drawable.placeholder_square)
        .transform(CenterCrop(), RoundedCorners(40))
        .into(this)
}

fun ImageView.loadCategoriesImage(imageUrl: String?) {
    GlideApp.with(this)
        .load(imageUrl)
        //.placeholder(R.drawable.placeholder_square)
        .transform(CenterCrop(), RoundedCorners(30))
        .into(this)
}

// This extension allow to call a function directly on the View to load an image from drawables.
fun ImageView.loadDrawableImage(drawable: Int) {
    GlideApp.with(this)
        .load(drawable)
        //.placeholder(R.drawable.placeholder_square)
        .fitCenter()
        .into(this)
}

fun ImageView.setSvgColor(@ColorRes color: Int) =
    setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)