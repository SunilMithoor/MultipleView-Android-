package com.app.presentation.module

import android.content.Context
import android.content.Intent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    init {
        Timber.d("Navigator init")
    }

    fun startActivity(
        context: Context?,
        className: Class<*>,
        flags: Int? = null,
        isClearTask: Boolean = false
    ) {
        context?.let {
            val intent = Intent(context, className)
            if (isClearTask) {
                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            if (flags != null) {
                intent.flags = flags
            }
            it.startActivity(intent)
        }
    }
}