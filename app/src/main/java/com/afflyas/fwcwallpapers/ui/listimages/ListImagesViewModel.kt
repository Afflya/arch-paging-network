package com.afflyas.fwcwallpapers.ui.listimages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afflyas.fwcwallpapers.repository.PixabayImage
import com.afflyas.fwcwallpapers.repository.PixabayImagesRepository
import com.afflyas.fwcwallpapers.repository.RepoResponse
import javax.inject.Inject

/**
 *
 * ViewModel that stores String to search
 * and api response as LiveData
 *
 */
class ListImagesViewModel @Inject constructor(private val pixabayImagesRepository: PixabayImagesRepository) : ViewModel() {

    val term = MutableLiveData<String>()

    val searchResult = MutableLiveData<RepoResponse<List<PixabayImage>>>()

    /**
     * set termString and execute search request
     * @param termString - string to search for
     */
    fun loadImages(termString: String?) {
        if (termString != null && !termString.isEmpty()) {
            term.value = termString
            pixabayImagesRepository.loadImages(searchResult, termString)
        }
    }

    /**
     * execute request with the same termString
     */
    fun refresh() {
        loadImages(term.value)
    }
}
