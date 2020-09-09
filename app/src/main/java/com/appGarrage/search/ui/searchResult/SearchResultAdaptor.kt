package com.shipmedtechnologies.onetab.ui.searchResult

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appGarrage.search.R
import com.appGarrage.search.helper.hide
import com.appGarrage.search.helper.loadBannerImage
import com.appGarrage.search.helper.show
import com.shipmedtechnologies.onetab.data.remote.searchResult.model.Data
import kotlinx.android.synthetic.main.item_search_result.view.*

class SearchResultAdaptor (var searchResultAdaptorInterface: SearchResultAdaptorInterface) :
    RecyclerView.Adapter<SearchResultAdaptor.SearchResultItemViewHolder>() {

    private var searchResultResponseList: ArrayList<Data>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        return SearchResultItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bind(searchResultResponseList?.get(position))

        holder.addLayout.setOnClickListener {
            searchResultResponseList?.get(position)?.productId?.toInt()?.let { it1 ->
                searchResultAdaptorInterface.onAddProductBtnClicked(
                    position,
                    it1,
                    "0"
                )
            }
        }

        holder.addBtn.setOnClickListener {
            searchResultResponseList?.get(position)?.productId?.toInt()?.let { it1 ->
                searchResultAdaptorInterface.onAddProductBtnClicked(
                    position,
                    it1,
                    holder.productQuantity.text.toString()
                )
            }
        }

        holder.minusBtn.setOnClickListener {
            searchResultResponseList?.get(position)?.productId?.toInt()?.let { it1 ->
                searchResultAdaptorInterface.onMinusProductBtnClicked(
                    position,
                    it1,
                    holder.productQuantity.text.toString()
                )
            }
        }

        holder.itemView.setOnClickListener {
            if ( searchResultResponseList?.get(position)?.type.toString() == "brand") {
//                val intent = Intent(holder.itemView.context, ProductsActivity::class.java)
//                intent.putExtra(AppConstants.categoryId,  searchResultResponseList?.get(position)?.brandId.toString())
//                intent.putExtra(AppConstants.offer_Id, AppConstants.VALUE_OFFER)
//                intent.putExtra(AppConstants.toolbarTitle,  searchResultResponseList?.get(position)?.brandName)
//                holder.itemView.context.startActivity(intent)
            } else if ( searchResultResponseList?.get(position)?.type.toString() == "category") {
//                val intent = Intent(holder.itemView.context, ProductsActivity::class.java)
//                intent.putExtra(AppConstants.offer_Id, AppConstants.VALUE_CATEGORY)
//                intent.putExtra(AppConstants.categoryId,  searchResultResponseList?.get(position)?.categoryId.toString())
//                intent.putExtra(AppConstants.toolbarTitle,  searchResultResponseList?.get(position)?.categoryName)
//                holder.itemView.context.startActivity(intent)
            } else {
                searchResultAdaptorInterface.onItemViewClicked(position, searchResultResponseList?.get(position)?.productId)
            }
        }
    }

    override fun getItemCount(): Int {
        if (searchResultResponseList != null) {
            return searchResultResponseList?.size!!
        } else {
            return 0
        }
    }


    fun setItem(resultResponse: ArrayList<Data>?) {
        searchResultResponseList = resultResponse
        notifyDataSetChanged()
    }

    class SearchResultItemViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val productImg: ImageView = itemView.search_result_product_iv
        val productName: TextView = itemView.search_result_title
        val productDes: TextView = itemView.search_result_company_tv
        val productQuantity: TextView = itemView.serach_result_cart_product_quantity
        val productPrice: TextView = itemView.search_result_cart_product_price_tv
        val strikeAmount: TextView = itemView.strike_amount_tv
        val discount: TextView = itemView.discount_tv
        val minusBtn: ImageButton = itemView.serach_result_cart_product_remove_btn
        val addBtn: ImageButton = itemView.serach_result_cart_product_add_btn

        val addLayout: LinearLayout = itemView.add_to_cart_layout
        val arrowIcon: ImageView = itemView.arrow_iv

        fun bind(searchData: Data?) {
            if (searchData?.type.toString() == "brand") {
                strikeAmount.hide()
                discount.hide()
                addLayout.hide()
                arrowIcon.show()
                productImg.hide()
                productPrice.hide()
                productQuantity.hide()
                minusBtn.hide()
                addBtn.hide()
                productPrice.hide()
                productQuantity.hide()
                productDes.text = "in TOP BRANDS"
                productName.text = searchData?.brandName.toString()
                productDes.setTextColor(Color.parseColor("#0aa156"))
                productDes.setTypeface(Typeface.DEFAULT_BOLD)
            } else if (searchData?.type.toString() == "category") {
                addLayout.hide()
                strikeAmount.hide()
                discount.hide()
                arrowIcon.show()
                productImg.hide()
                productPrice.hide()
                productQuantity.hide()
                minusBtn.hide()
                addBtn.hide()
                productPrice.hide()
                productQuantity.hide()
                productDes.text = "in CATEGORIES"
                productName.text = searchData?.categoryName.toString()
                productDes.setTextColor(Color.parseColor("#0aa156"))
                productDes.setTypeface(Typeface.DEFAULT_BOLD)
            } else {
                strikeAmount.show()
                discount.show()
                addLayout.show()
                arrowIcon.hide()
                productImg.show()
                productPrice.show()
                productImg.loadBannerImage(searchData?.image)
                productName.text = searchData?.productName.toString()
                val strikeProductPrice = SpannableString("₹${searchData?.maximumRetailPrice}")
                strikeProductPrice.setSpan(StrikethroughSpan(), 0, strikeProductPrice.length, 0)
                productDes.text = "${searchData?.quantityUnit ?: ""} ${searchData?.typeUnit?.name.toString()}"
                productDes.setTextColor(Color.parseColor("#000000"))
                productDes.setTypeface(Typeface.DEFAULT)
                productPrice.text = "₹"+searchData?.offerPrice.toString()

                if (searchData?.maximumRetailPrice == searchData?.offerPrice) {
                    strikeAmount.hide()
                } else {
                    strikeAmount.show()
                    strikeAmount.text = strikeProductPrice
                }
                if (searchData?.discountValue ?: 0 > 0) {
                    discount.show()
                    discount.text = "${searchData?.discountValue}% off"
                } else {
                    discount.hide()
                }

                if (searchData?.cartQuantity ?: 0 > 0) {
                    minusBtn.show()
                    addBtn.show()
                    productQuantity.show()
                    productQuantity.text = searchData?.cartQuantity.toString()
                    addLayout.hide()
                } else {
                    minusBtn.hide()
                    addBtn.hide()
                    productQuantity.hide()
                    addLayout.show()
                }
            }
        }
    }
}