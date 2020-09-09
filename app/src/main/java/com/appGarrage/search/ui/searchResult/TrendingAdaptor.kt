package com.shipmedtechnologies.onetab.ui.searchResult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appGarrage.search.R
import kotlinx.android.synthetic.main.item_trending.view.*

class TrendingAdaptor (var trendingAdaptorInterface: TrendingAdaptorInterface) :
    RecyclerView.Adapter<TrendingAdaptor.TrendingItemViewHolder>() {

    private var itemList: ArrayList<String>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingItemViewHolder {
        return TrendingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendingItemViewHolder, position: Int) {
        holder.bind(itemList?.get(position))
        holder.itemView.setOnClickListener {
            itemList?.get(position)?.let { it1 ->
                trendingAdaptorInterface.onItemClicked(position,
                    it1
                )
            }
        }
    }

    override fun getItemCount(): Int {
        if (itemList != null) {
            return itemList?.size!!
        } else {
            return 0
        }
    }

    fun setItem(resultResponse: ArrayList<String>?) {
        itemList = resultResponse
        notifyDataSetChanged()
    }

    class TrendingItemViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title_tv
        fun bind(searchData: String?) {
            title.text = searchData
        }
    }
}