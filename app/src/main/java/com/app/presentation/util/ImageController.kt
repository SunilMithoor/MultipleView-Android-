package com.app.presentation.util

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso


class ImageController(private val imgMain: AppCompatImageView) {
    fun setImgMain(path: Uri?) {
        Picasso
            .get()
            .load(path)
            .fit()
            .centerCrop()
            .into(imgMain)
    }
}