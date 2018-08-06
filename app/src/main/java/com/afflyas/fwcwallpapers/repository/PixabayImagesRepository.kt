package com.afflyas.fwcwallpapers.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.afflyas.fwcwallpapers.api.ApiResponse
import com.afflyas.fwcwallpapers.api.PixabayApiService
import com.afflyas.fwcwallpapers.core.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 *
 * Repository that handles [PixabayImage] objects.
 *
 * Request result to api stores in searchResult
 * that should be observed by [ListImagesViewModel]
 *
 * Response contains [RepoResponseStatus] with current status
 * and List<PixabayImage> with the list of images found
 *
 */
class PixabayImagesRepository @Inject constructor(private val apiService: PixabayApiService){

    var searchResult: MutableLiveData<RepoResponse<List<PixabayImage>>>? = null

    fun loadImages(searchResult: MutableLiveData<RepoResponse<List<PixabayImage>>>, term: String){
        this.searchResult = searchResult

        searchResult.postValue(RepoResponse.loading())

        apiService.getImages(term).enqueue(responseCallback)
    }

    private val responseCallback = object : Callback<ApiResponse<PixabayImage>> {

        override fun onResponse(call: Call<ApiResponse<PixabayImage>>?, response: Response<ApiResponse<PixabayImage>>?) {
            Log.d(App.DEV_TAG, javaClass.simpleName + " onResponse")
            val responseBody = response?.body()

            if (responseBody == null || responseBody.total == 0) {
                searchResult?.postValue(RepoResponse.success(null))
            } else {
                searchResult?.postValue(RepoResponse.success(responseBody.items))
            }
        }

        override fun onFailure(call: Call<ApiResponse<PixabayImage>>?, t: Throwable?) {
            Log.d(App.DEV_TAG, javaClass.simpleName + " onFailure")
            searchResult?.postValue(RepoResponse.error())
        }
    }

}