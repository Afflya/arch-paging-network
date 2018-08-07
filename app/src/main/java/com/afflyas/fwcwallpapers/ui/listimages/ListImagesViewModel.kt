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

    private val query = MutableLiveData<String>()

    private val repoResult = map(query){
        repository.loadImages(it)
    }

    val images = switchMap(repoResult) { it.pagedList }!!
    val networkState = switchMap(repoResult) { it.networkState }!!
    val refreshState = switchMap(repoResult) { it.refreshState }!!

    fun currentQuery(): String? = query.value

    fun loadImages(queryString: String?) : Boolean{
        if (query.value == queryString) {
            return false
        }
        query.value = queryString
        return true
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }


}
