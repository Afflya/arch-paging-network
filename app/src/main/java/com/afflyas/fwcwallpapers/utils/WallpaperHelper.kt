package com.afflyas.fwcwallpapers.utils

import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.afflyas.fwcwallpapers.core.MainActivity


object WallpaperHelper {
    /**
     * Setup wallpaper bitmap to crop from centre
     *
     * Source:
     * https://stackoverflow.com/questions/25699168/set-wallpaper-with-bitmap-avoid-crop-and-set-fit-center
     */
    fun cropBitmapFromCenterAndScreenSize(mainActivity: MainActivity, bitmap: Bitmap): Bitmap {
        var bitmap = bitmap
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        val displayMetrics = DisplayMetrics()
        mainActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val bitmapRatio = bitmapWidth / bitmapHeight
        val screenRatio = screenWidth / screenHeight
        val bitmapNewWidth: Int
        val bitmapNewHeight: Int

        if (screenRatio > bitmapRatio) {
            bitmapNewWidth = screenWidth
            bitmapNewHeight = (bitmapNewWidth / bitmapRatio).toInt()
        } else {
            bitmapNewHeight = screenHeight
            bitmapNewWidth = (bitmapNewHeight * bitmapRatio).toInt()
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapNewWidth,
                bitmapNewHeight, true)

        val bitmapGapX: Int
        val bitmapGapY: Int
        bitmapGapX = ((bitmapNewWidth - screenWidth) / 2.0f).toInt()
        bitmapGapY = ((bitmapNewHeight - screenHeight) / 2.0f).toInt()

        bitmap = Bitmap.createBitmap(bitmap, bitmapGapX, bitmapGapY,
                screenWidth, screenHeight)
        return bitmap
    }
}