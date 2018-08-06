package com.afflyas.fwcwallpapers.repository

/**
 * Status of a data from repository that is provided to the UI.
 *
 * Created by the [PixabayImagesRepository] that returns
 * `LiveData<RepoResponse<PixabayImage>>` to pass back the data to the UI with its status.
 */
enum class RepoResponseStatus {
    SUCCESS,
    ERROR,
    LOADING
}