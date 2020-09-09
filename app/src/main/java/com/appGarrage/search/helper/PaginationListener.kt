package com.appGarrage.search.helper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationListener(private val layoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            val layoutManager = layoutManager
            val visibleItemCount = layoutManager.findLastCompletelyVisibleItemPosition() + 1
            if (visibleItemCount == layoutManager.itemCount) {
                log("pagination last page  value::::::${!isLastPage()}")
                log("pagination loading  value::::::${!isLoading()}")
                if (!isLastPage()) {
                    if (!isLoading()) {
                        loadMoreItems()
                    }
                }
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    companion object {
        const val PAGE_START = 1
    }

}

abstract class VerticalPaginationListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount;
        val totalItemCount = layoutManager.itemCount;
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        log("pagination last page  value::::::${!isLastPage()}")
        log("pagination loading  value::::::${!isLoading()}")
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems();
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    companion object {
        const val PAGE_START = 1
    }

}