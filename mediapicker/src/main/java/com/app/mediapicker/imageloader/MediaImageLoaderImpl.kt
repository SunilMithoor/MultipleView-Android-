package com.app.mediapicker.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.app.mediapicker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author TUNGDX
 */

class MediaImageLoaderImpl(context: Context) : MediaImageLoader {

    override fun displayImage(uri: Uri, imageView: ImageView) {
        val requestOptions = RequestOptions().placeholder(R.drawable.ic_loading)
        Glide.with(imageView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(uri).into(imageView)
    }
}