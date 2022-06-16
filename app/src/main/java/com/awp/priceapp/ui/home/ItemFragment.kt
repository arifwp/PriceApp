package com.awp.priceapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.priceapp.adapter.ProductsAdapter
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.databinding.ActivityAddProductBinding
import com.awp.priceapp.databinding.FragmentItemBinding
import com.awp.priceapp.response.ListProduct
import com.awp.priceapp.response.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemFragment : Fragment() {

    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemViewModel: ItemViewModel

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter = ProductsAdapter()
        productsAdapter.notifyDataSetChanged()

//        binding.apply {
//            rvItem.setHasFixedSize(true)
//            rvItem.layoutManager = LinearLayoutManager(activity)
//            rvItem.adapter = productsAdapter
//        }

//        setRv()



//        itemViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ItemViewModel::class.java)


//        val client = ApiConfig.getApiService().getAllPost()
//        client.enqueue(object : Callback<ProductResponse> {
//            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
//                if (response.isSuccessful){
//                    val resource: ProductResponse? = response.body()
//                    updatingData(resource!!.data)
//                    Log.e("sambungan", response.body().toString())
//                } else {
//                    Log.e("sambunganelse", response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
//                Log.d("error_getlist", t.message.toString())
//            }
//
//        })

    }

//    private fun setRv() {
//        val linearLayoutManager = LinearLayoutManager(activity)
//        productsAdapter = ProductsAdapter()
//
//        recyclerView = binding.rvItem
//        recyclerView.apply {
//            adapter = productsAdapter
//            layoutManager = linearLayoutManager
//        }
//    }
//
//
//    private fun updatingData(listProduct: List<ListProduct>) {
//        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
//        productsAdapter.submitList(listProduct)
//
//        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
//    }


}
