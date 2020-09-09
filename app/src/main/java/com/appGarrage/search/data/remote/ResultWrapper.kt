package com.appGarrage.search.data.remote

sealed class ResultWrapper<out T> {
    data class Success<out T>(val code: Int? = null,val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    data class NetworkError(val networkError: String = "No Internet Connection") :
        ResultWrapper<Nothing>()
}
