package com.shipmedtechnologies.onetab.data.remote.searchResult.model


import com.google.gson.annotations.SerializedName

data class SearchResultResponse(
    @SerializedName("data")
    val data: ArrayList<Data>?
)