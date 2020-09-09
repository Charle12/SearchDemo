package com.shipmedtechnologies.onetab.data.remote.searchResult

import com.appGarrage.search.data.remote.ApiService
import com.appGarrage.search.data.remote.ResultWrapper
import com.appGarrage.search.data.remote.safeApiCall
import com.appGarrage.search.helper.AppConstants
import com.shipmedtechnologies.onetab.data.remote.searchResult.model.SearchResultResponse

class SearchResultRepositoryImpl constructor(private val apiService: ApiService) : SearchResultRepository {
    override suspend fun fetchSearchResultList(searchString: String): ResultWrapper<SearchResultResponse> {
        return safeApiCall { apiService.searchResultList(searchString, AppConstants.UTM_SOURCE, AppConstants.DEVICE_UDID) }
    }

}