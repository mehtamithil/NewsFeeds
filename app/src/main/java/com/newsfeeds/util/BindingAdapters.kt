package com.newsfeeds.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.newsfeeds.R

@BindingAdapter("imageUrl")
fun setImageUrl(img: ImageView, url: String?) {

    url?.let {

        loadImage(img.context, img, it, R.drawable.ic_placeholder, retry = false)

    }
}