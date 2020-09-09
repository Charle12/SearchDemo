package com.shipmedtechnologies.onetab.data.remote.searchResult.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("brand_id")
    val brandId: Int?,
    @SerializedName("brand_name")
    val brandName: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("discount_value")
    val discountValue: Int?,
    @SerializedName("cart_quantity")
    var cartQuantity: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("is_outofstock")
    val isOutofstock: Boolean?,
    @SerializedName("maximum_retail_price")
    val maximumRetailPrice: Double?,
    @SerializedName("offer_price")
    val offerPrice: Double?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("type_unit")
    val typeUnit: TypeUnit?,
    @SerializedName("quantity_unit")
    val quantityUnit: String?,
    @SerializedName("message")
    val message: String?
)