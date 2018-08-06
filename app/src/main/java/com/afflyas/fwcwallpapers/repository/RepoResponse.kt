package com.afflyas.fwcwallpapers.repository

/**
 * Object that contains response of executing api request and its current status
 *
 */
class RepoResponse<T>(private val status: RepoResponseStatus, private val data: T?) {

    fun getStatus(): RepoResponseStatus {
        return status
    }

    fun getData(): T? {
        return data
    }

    companion object {
        /**
         * Create response instance with data returned by successful response
         */
        fun <T> success(data: T?): RepoResponse<T> {
            return RepoResponse(RepoResponseStatus.SUCCESS, data)
        }

        /**
         * Create response instance that indicates that request was failed
         */
        fun <T> error(): RepoResponse<T> {
            return RepoResponse(RepoResponseStatus.ERROR, null)
        }

        /**
         * Create response instance that indicates that request execution started
         */
        fun <T> loading(): RepoResponse<T> {
            return RepoResponse(RepoResponseStatus.LOADING, null)
        }
    }

}