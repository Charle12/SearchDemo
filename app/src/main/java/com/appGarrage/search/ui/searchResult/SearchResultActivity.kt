package com.shipmedtechnologies.onetab.ui.searchResult

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appGarrage.search.databinding.ActivitySearchResultsBinding
import com.appGarrage.search.helper.hide
import com.appGarrage.search.helper.hideKeyboard
import com.appGarrage.search.helper.show
import com.appGarrage.search.helper.snack
import com.appGarrage.search.utils.DividerItemDecoration
import com.shipmedtechnologies.onetab.data.remote.searchResult.model.Data
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchResultActivity : AppCompatActivity(), SearchResultAdaptorInterface, TrendingAdaptorInterface {

    private var mPosition: Int = 0
    private lateinit var searchResultsBinding: ActivitySearchResultsBinding
    private lateinit var mRootView: View
    private val searchResultViewModel: SearchResultViewModel by viewModel()
    private lateinit var searchResultAdaptor: SearchResultAdaptor
    lateinit var trendingAdaptor: TrendingAdaptor
    private var searchResultResponseList: ArrayList<Data>? = ArrayList()
    private var itemList: ArrayList<String>? = ArrayList()
    var queryTextChangedJob: Job? = null
    private var cartClickedIndex: Int = 0
    private var cartQuantity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchResultsBinding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(searchResultsBinding.root)
        mRootView = findViewById(android.R.id.content)
        setSupportActionBar(searchResultsBinding.searchResultToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Search for product"

        searchResultsBinding.searchResultSrl.isEnabled = false

        searchResultsBinding.searchResultSearchEt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                queryTextChangedJob?.cancel()
                queryTextChangedJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500)
                    if (s.isEmpty()) {
                        searchResultsBinding.searchResultSrl.isEnabled = false
                        searchResultsBinding.searchResultTitleTv.text = "Trending Searches"
                        searchResultsBinding.serachResultListRv.hide()
                        searchResultsBinding.trendingRv.show()
                        trendingAdaptor.setItem(itemList)
                        trendingAdaptor.notifyDataSetChanged()
                    } else {
                        searchResultsBinding.searchResultTitleTv.text = "Search Results"
                        searchResultViewModel.requestSearchResultList(s.toString())
                        searchResultsBinding.searchResultSrl.isEnabled = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        searchResultsBinding.searchResultSearchEt.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event.action === KeyEvent.ACTION_DOWN
                            && event.keyCode === KeyEvent.KEYCODE_ENTER)
                ) {
                    v.hideKeyboard()
                    return@OnEditorActionListener true
                }
                // Return true if you have consumed the action, else false.
                false
            })

        initSearchResultRecycler()
        initTrendingSearchRecycler()
        initViewModel()

        searchResultsBinding.searchResultSrl.setOnRefreshListener {
            if (searchResultsBinding.searchResultSearchEt.text.toString()
                    .trim().length > 2 && searchResultResponseList?.size!! > 0
            ) {
                searchResultResponseList?.clear()
                searchResultViewModel.requestSearchResultList(searchResultsBinding.searchResultSearchEt.text.toString())
            }
        }
    }

    private fun initTrendingSearchRecycler() {
        searchResultsBinding.serachResultListRv.hide()
        searchResultsBinding.trendingRv.show()
        searchResultsBinding.searchResultTitleTv.text = "Trending Searches"
        trendingAdaptor = TrendingAdaptor(this)
        searchResultsBinding.trendingRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = trendingAdaptor
        }
        setTrendingSearch()
    }

    private fun setTrendingSearch() {
        itemList?.add("N95 Mask")
        itemList?.add("Sanitizer")
        itemList?.add("Ecosprin 75mg Tab 14's")
        itemList?.add("Mask")
        itemList?.add("homemade cloth face mask")
        itemList?.add("surgical mask")

        trendingAdaptor.setItem(itemList)
        trendingAdaptor.notifyDataSetChanged()
    }
    private fun initSearchResultRecycler() {
        val dividerItemDecoration = DividerItemDecoration(
            this,
            RecyclerView.VERTICAL
            , false
        )
        searchResultsBinding.serachResultListRv.show()
        searchResultsBinding.trendingRv.hide()
        searchResultAdaptor = SearchResultAdaptor(this)
        searchResultsBinding.serachResultListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(dividerItemDecoration)
            adapter = searchResultAdaptor
        }
    }

    private fun initViewModel() {

        searchResultViewModel.onSuccessResultListData.observe(this, Observer {
            searchResultsBinding.serachResultListRv.show()
            mRootView.hideKeyboard()
            searchResultsBinding.trendingRv.hide()
            if (it.data?.isEmpty()!!) {
                searchResultsBinding.searchResultTitleTv.hide()
            } else {
                searchResultResponseList = it.data
                val searchData: Data? = searchResultResponseList?.get(0)
                searchResultsBinding.searchResultTitleTv.show()
                if (searchData?.message != null) {
                    searchResultAdaptor.setItem(ArrayList())
                    searchResultAdaptor.notifyDataSetChanged()
                    searchResultsBinding.searchResultTitleTv.text = searchData?.message
                } else {
                    searchResultAdaptor.setItem(searchResultResponseList)
                    searchResultAdaptor.notifyDataSetChanged()
                    searchResultsBinding.searchResultTitleTv.text = "Search Results"
                }
            }
            searchResultsBinding.searchResultSrl.isRefreshing = false
        })

//        searchResultViewModel.onLoading.observe(this, Observer { showLoading ->
//            progress_overlay.visibility = if (showLoading!!) View.VISIBLE else View.GONE
//        })

        searchResultViewModel.onStringError.observe(this, Observer { onNoNetwork ->
            if (!onNoNetwork.isEmpty()) {
                mRootView.snack(onNoNetwork)
            }
        })

        searchResultViewModel.onNetworkError.observe(this, Observer { onNoNetwork ->
            searchResultsBinding.searchResultTitleTv.hide()
            if (onNoNetwork == true) {
                mRootView.snack(
                    "No Internet Connection\n" +
                            "Network Error!"
                )
            }
        })

        searchResultViewModel.onError.observe(this, Observer { errorCode ->
            when (errorCode) {
                401 -> {
                }
                200 -> {
                }
                else -> {
                    searchResultsBinding.searchResultTitleTv.hide()
                    mRootView.snack("Something went wrong!!!")
                }
            }
        })
    }
    private fun goToLogin() {
//        val intent = Intent(this, LoginActivity::class.java).apply {
//            putExtra("IS_REGULAR_FLOW", false)
//            putExtra("GO_TO_FRAGMENT", "dashboard")
//        }
//        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    override fun onAddProductBtnClicked(position: Int, productId: Int, quantity: String) {
//        this.mPosition = position
//        var userSelectedQuantity = 1
//        if (userSelectedQuantity < AppConstants.MAX_CART_QUANTITY_ALLOWED) {
//            cartClickedIndex = position
//            cartQuantity = quantity.toInt()
//            val addToCartRequest =
//                AddToCartRequest(productId = productId, deviceUdid = AppConstants.DEVICE_UDID, utmSource = AppConstants.UTM_SOURCE)
//            searchResultViewModel.requestAddToCart(addToCartRequest)
//        } else {
//            mRootView.snack("Quantity is greater than stock")
//        }
    }

    override fun onMinusProductBtnClicked(position: Int, productId: Int, quantity: String) {
//        this.mPosition = position
//        cartClickedIndex = position
//        cartQuantity = quantity.toInt()
//        val removeFromCartRequest =
//            RemoveFromCartRequest(productId = productId, deviceUdid = AppConstants.DEVICE_UDID, utmSource = AppConstants.UTM_SOURCE)
//        searchResultViewModel.requestRemoveFromCart(removeFromCartRequest)
    }

    override fun onItemViewClicked(position: Int, productId: Int?) {

    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    override fun onItemClicked(position: Int, searchTxt: String) {
        searchResultViewModel.requestSearchResultList(searchTxt.toString())
        searchResultsBinding.searchResultSrl.isEnabled = true
        searchResultsBinding.searchResultSearchEt.setText(searchTxt)
    }
}