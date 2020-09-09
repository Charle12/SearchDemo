package com.appGarrage.search.di.modules

import com.shipmedtechnologies.onetab.data.remote.searchResult.SearchResultRepository
import com.shipmedtechnologies.onetab.data.remote.searchResult.SearchResultRepositoryImpl
import com.shipmedtechnologies.onetab.ui.searchResult.SearchResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


var appModule = module {

    // Serach Result List
    factory<SearchResultRepository> { SearchResultRepositoryImpl(get()) }
    viewModel { SearchResultViewModel(searchResultRepository = get()) }
}