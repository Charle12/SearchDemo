package com.shipmedtechnologies.onetab.data.remote.searchResult.model


import com.google.gson.annotations.SerializedName

data class TypeUnit(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)