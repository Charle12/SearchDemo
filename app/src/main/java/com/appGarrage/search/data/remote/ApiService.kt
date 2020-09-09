package com.appGarrage.search.data.remote

import com.shipmedtechnologies.onetab.data.remote.searchResult.model.SearchResultResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @GET("/api/medicine/search/")
    suspend fun searchResultList(
        @Query("keyword") data: String,
        @Query("utm_source") utmSource: String,
        @Query("device_udid") deviceUdid: String
    ): SearchResultResponse
}