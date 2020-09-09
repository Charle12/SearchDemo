package com.shipmedtechnologies.onetab.data.remote.searchResult

import com.appGarrage.search.data.remote.ResultWrapper
import com.shipmedtechnologies.onetab.data.remote.searchResult.model.SearchResultResponse

interface SearchResultRepository {
    suspend fun fetchSearchResultList(searchString: String) : ResultWrapper<SearchResultResponse>
}