package com.afflyas.fwcwallpapers.ui.common

import com.afflyas.fwcwallpapers.repository.PixabayImage

/**
 * Interface for RecyclerView item to open ImageFragment
 */
interface ItemClickCallback {
    fun onItemClick(pixabayImage: PixabayImage)
}