package com.afflyas.fwcwallpapers.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.afflyas.fwcwallpapers.api.PixabayApiService
import com.afflyas.fwcwallpapers.repository.PageKeyedImageDataSource
import com.afflyas.fwcwallpapers.repository.PixabayImage
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class ImageDataSourceFactory(
        private val networkExecutor: Executor,
        private val pixabayApiService: PixabayApiService,
        private val query: String) : DataSource.Factory<Int, PixabayImage>() {

    val sourceLiveData = MutableLiveData<PageKeyedImageDataSource>()

    override fun create(): DataSource<Int, PixabayImage> {
        val source = PageKeyedImageDataSource(networkExecutor, pixabayApiService, query)
        sourceLiveData.postValue(source)
        return source
    }

}