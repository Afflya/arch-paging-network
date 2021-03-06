package com.afflyas.fwcwallpapers.api

import com.afflyas.fwcwallpapers.repository.PixabayImage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

    companion object {
        const val API_KEY = "9711400-51a476dcb6d8ccb45a56bedb6"
        const val BASE_URL = "https://pixabay.com/api/"

        const val DEFAULT_PAGE_SIZE = 20
    }

    /**
     *
     * Search for images using pagination
     *
     * @param query - string you want to search for
     * @param page - number of page to load
     * @return ApiResponse with list of PixabayImage
     *
     */
    @GET("?key=$API_KEY")
    fun getImages(
            @Query("q") query: String,
            //@Query("per_page") pageSize: Int,
            @Query("page") page: Int): Call<ApiResponse<PixabayImage>>

}