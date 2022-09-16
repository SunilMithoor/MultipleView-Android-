package com.app.mediapicker

import com.app.mediapicker.MediaItem

/**
 * @author TUNGDX
 */

/**
 * Listener for select media item.
 *
 */
interface MediaSelectedListener {

    fun onHasNoSelected()

    fun onHasSelected(mediaSelectedList: List<MediaItem>)
}
