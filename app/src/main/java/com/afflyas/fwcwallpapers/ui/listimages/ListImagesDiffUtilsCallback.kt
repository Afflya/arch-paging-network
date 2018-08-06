package com.afflyas.fwcwallpapers.ui.listimages

import androidx.recyclerview.widget.DiffUtil
import com.afflyas.fwcwallpapers.repository.PixabayImage

/**
 *
 * Used by [ListImagesAdapter] to calculate difference between lists
 *
 */
class ListImagesDiffUtilsCallback(private val oldImagesList: List<PixabayImage>, private val newImagesList: List<PixabayImage>) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImagesList[oldItemPosition].id == newImagesList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldImagesList.size
    }

    override fun getNewListSize(): Int {
        return newImagesList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImagesList[oldItemPosition] == newImagesList[newItemPosition]
    }

}