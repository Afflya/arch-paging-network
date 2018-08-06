package com.afflyas.fwcwallpapers.paging.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.afflyas.fwcwallpapers.api.PixabayApiService
import com.afflyas.fwcwallpapers.core.AppExecutors
import com.afflyas.fwcwallpapers.paging.repository.ImageDataSourceFactory
import com.afflyas.fwcwallpapers.paging.repository.Listing
import com.afflyas.fwcwallpapers.repository.PixabayImage
import javax.inject.Inject

/**
 * Repository implementation that returns a Listing that loads data directly from network by using
 * the previous / next page keys.
 */
class PageKeyedImageRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val pixabayApiService: PixabayApiService
){

    fun loadImages(query: String) : Listing<PixabayImage> {
        val sourceFactory = ImageDataSourceFactory(appExecutors.networkIO(), pixabayApiService, query)

        val livePagedList = LivePagedListBuilder(sourceFactory, PixabayApiService.DEFAULT_PAGE_SIZE)
                // provide custom executor for network requests, otherwise it will default to
                // Arch Components' IO pool which is also used for disk access
                .setFetchExecutor(appExecutors.networkIO())
                .build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )

    }

}