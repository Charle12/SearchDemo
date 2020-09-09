package com.shipmedtechnologies.onetab.ui.searchResult

interface SearchResultAdaptorInterface {
    fun onAddProductBtnClicked(position: Int, productId: Int, quantity: String)
    fun onMinusProductBtnClicked(position: Int, productId: Int, quantity: String)
    fun onItemViewClicked(
        position: Int,
        productId: Int?
    )
}