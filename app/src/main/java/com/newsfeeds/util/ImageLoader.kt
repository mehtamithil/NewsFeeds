package com.newsfeeds.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.newsfeeds.R
import java.io.FileNotFoundException
import java.io.IOException

fun loadImage(activity: Context?, img: ImageView?, url: String?, imgResIdPlaceHolder: Int? = null, retry: Boolean = true) {
    Glide.with(activity)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .dontAnimate()
        .placeholder(imgResIdPlaceHolder ?: R.color.translucent)
        .listener(object : RequestListener<String?, GlideDrawable?> {

            override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable?>?,
                                     isFirstResource: Boolean): Boolean {
                if (activity != null && !(e is FileNotFoundException || e is IOException) && retry) {
                    loadImage(activity, img, url, imgResIdPlaceHolder, false)
                }
                imgResIdPlaceHolder?.let { img?.setImageResource(it) }
                return false
            }

            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable?>?,
                                         isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(img)
}