package com.shipmedtechnologies.onetab.ui.searchResult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appGarrage.search.data.remote.ResultWrapper
import com.shipmedtechnologies.onetab.data.remote.searchResult.SearchResultRepository
import com.shipmedtechnologies.onetab.data.remote.searchResult.model.SearchResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultViewModel (val searchResultRepository: SearchResultRepository) : ViewModel() {

    var searchString = ""
    val onLoading = MutableLiveData<Boolean>()
    val onNetworkError = MutableLiveData<Boolean>()
    val onSuccessResultListData = MutableLiveData<SearchResultResponse>()
    val onError = MutableLiveData<Int>()
    val onStringError = MutableLiveData<String>()

    fun requestSearchResultList(searchString: String) {
        viewModelScope.launch {
            onLoading.value = true
            onNetworkError.value = false
            onError.value = 200
            val response =
                withContext(Dispatchers.IO) { searchResultRepository.fetchSearchResultList(searchString) }
            onLoading.value = false
            when (response) {
                is ResultWrapper.Success -> onSuccessResultListData.value = response.value
                is ResultWrapper.GenericError -> onError.value = response.code
                is ResultWrapper.NetworkError -> onNetworkError.value = true
            }
        }
    }
}
