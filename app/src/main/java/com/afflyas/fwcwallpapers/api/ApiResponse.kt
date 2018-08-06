package com.afflyas.fwcwallpapers.api

import com.google.gson.annotations.SerializedName

/**
 * Response data returned by Api
 */
data class ApiResponse<T>(
        @SerializedName("totalHits") val total: Int,
        @SerializedName("hits") val items: List<T>
)