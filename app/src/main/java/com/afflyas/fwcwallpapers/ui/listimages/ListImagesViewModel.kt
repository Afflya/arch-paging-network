package com.afflyas.fwcwallpapers.ui.listimages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.afflyas.fwcwallpapers.repository.PageKeyedImageRepository
import javax.inject.Inject

/**
 *
 * ViewModel that stores String to search
 * and api response as LiveData
 *
 */
class ListImagesViewModel @Inject constructor(private val repository: PageKeyedImageRepository) : ViewModel() {

    /**
     * String to search for
     */
    private val query = MutableLiveData<String>()

    /**
     * the LiveData of Listing from repository
     */
    private val repoResult = map(query){
        repository.loadImages(it)
    }

    /**
     * the LiveData of paged lists for the UI to observe
     */
    val images = switchMap(repoResult) { it.pagedList }!!
    /**
     * represents the refresh status to show to the user. Separate from networkState, this
     * value is importantly only when refresh is requested.
     */
    val networkState = switchMap(repoResult) { it.networkState }!!
    /**
     * represents the network request status to show to the user
     */
    val refreshState = switchMap(repoResult) { it.refreshState }!!

    /**
     * get current query value
     */
    fun currentQuery(): String? = query.value

    /**
     * set queryString and execute search request if it is not the same as current query
     *
     * @param queryString - string to search for
     * @return request has been accepted
     */
    fun loadImages(queryString: String?) : Boolean{
        if (query.value == queryString) {
            return false
        }
        query.value = queryString
        return true
    }

    /**
     * refreshes the whole data and fetches it from scratch.
     */
    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    /**
     * retries any failed requests.
     */
    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }


}
