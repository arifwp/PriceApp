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

        binding.apply {
            rvItem.setHasFixedSize(true)
            rvItem.layoutManager = LinearLayoutManager(activity)
            rvItem.adapter = productsAdapter
        }

//        itemViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ItemViewModel::class.java)


//        val client = ApiConfig.getApiService().getAllPost()
//        client.enqueue(object : Callback<ListProduct> {
//            override fun onResponse(call: Call<ListProduct>, response: Response<ListProduct>) {
//                if (response.isSuccessful){
////                    val resource: ProductResponse? = response.body()
////                    updatingData(resource!!.listProduct)
//                    Log.e("sambungan", response.body().toString())
////                    listFollowers.postValue(response.body())
//                } else {
//                    Log.e("sambunganelse", response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<ListProduct>, t: Throwable) {
//                Log.d("error_getlist", t.message.toString())
//            }
//
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRv() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        productsAdapter = ProductsAdapter()

        recyclerView = binding.rvItem
        recyclerView.apply {
            adapter = productsAdapter
            layoutManager = linearLayoutManager
        }
    }


    private fun updatingData(listProduct: List<ListProduct>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        productsAdapter.submitList(listProduct)

        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }



}
