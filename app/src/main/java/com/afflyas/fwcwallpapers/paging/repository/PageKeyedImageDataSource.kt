package com.afflyas.fwcwallpapers.paging.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.afflyas.fwcwallpapers.api.ApiResponse
import com.afflyas.fwcwallpapers.api.PixabayApiService
import com.afflyas.fwcwallpapers.core.App
import com.afflyas.fwcwallpapers.repository.PixabayImage
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class PageKeyedImageDataSource (
        private val networkExecutor: Executor,
        private val pixabayApiService: PixabayApiService,
        private val query: String) : PageKeyedDataSource<Int, PixabayImage>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            networkExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PixabayImage>) {
        Log.d(App.DEV_TAG, javaClass.simpleName + " loadInitial")

        val request = pixabayApiService.getImages(
                query = query,
                page = 1
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            //todo разобраться, почему execute()
            val response = request.execute()

            val responseBody = response?.body()


            /**
             * next page exists only when total hits > page size
             */
            val nextPageExists = if(responseBody != null){
                //responseBody.total > params.requestedLoadSize
                responseBody.total > PixabayApiService.DEFAULT_PAGE_SIZE
            }else{
                false
            }


            val items = responseBody?.items ?: emptyList()

            retry = null

            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            /**
             * No previous page -> null
             * Next page if exists -> 2
             */
            callback.onResult(items, null, if (nextPageExists) 2 else null)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PixabayImage>) {
        Log.d(App.DEV_TAG, javaClass.simpleName + " loadAfter")

        networkState.postValue(NetworkState.LOADING)

        val currentPage: Int = params.key

        val request = pixabayApiService.getImages(
                query = query,
                page = params.key
        )

        request.enqueue(object : retrofit2.Callback<ApiResponse<PixabayImage>>{
            override fun onResponse(call: Call<ApiResponse<PixabayImage>>, response: Response<ApiResponse<PixabayImage>>) {
                Log.d(App.DEV_TAG, javaClass.simpleName + " onResponse")
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    //currentPage

                    val nextPage: Int?

                    nextPage = if(responseBody == null) {
                        null
                    }else{

                        //Количество загруженных картинок
                        val totalLoaded = currentPage * PixabayApiService.DEFAULT_PAGE_SIZE

                        if(responseBody.total > totalLoaded){
                            currentPage + 1
                        }else{
                            null
                        }
                    }


                    val items = responseBody?.items ?: emptyList()

                    retry = null
                    callback.onResult(items,nextPage)
                    networkState.postValue(NetworkState.LOADED)
                }else{
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(
                            NetworkState.error("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<PixabayImage>>?, t: Throwable?) {
                Log.d(App.DEV_TAG, javaClass.simpleName + " onFailure")
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t?.message ?: "unknown error"))
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PixabayImage>) {
        // ignored, since we only ever append to our initial load
        Log.d(App.DEV_TAG, javaClass.simpleName + " loadBefore")
    }

}