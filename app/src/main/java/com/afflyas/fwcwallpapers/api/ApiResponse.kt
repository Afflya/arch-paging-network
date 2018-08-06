package com.afflyas.fwcwallpapers.api

import com.google.gson.annotations.SerializedName

/**
 * Response data returned by Api
 */
data class ApiResponse<T>(
        @SerializedName("totalHits") val resultCount: Int,
        @SerializedName("hits") val results: List<T>
)