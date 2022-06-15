package com.awp.priceapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.priceapp.adapter.ProductsAdapter
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.databinding.FragmentItemBinding
import com.awp.priceapp.response.ListProduct
import com.awp.priceapp.response.ProductResponse
import retrofit2.Call
import retrofit2.Response

class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemViewModel: ItemViewModel

    private val _list = MutableLiveData<ProductResponse>()
    val list: LiveData<ProductResponse> = _list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter = ProductsAdapter()
        productsAdapter.notifyDataSetChanged()

//        binding.apply {
////            rvItem.setHasFixedSize(true)
//            rvItem.layoutManager = LinearLayoutManager(activity)
//            rvItem.adapter = productsAdapter
//        }

        setRv()

//        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)


        val client = ApiConfig.getApiService().getAllPost()

        client.enqueue(object : retrofit2.Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful){
                    val resource: ProductResponse? = response.body()
                    updatingData(resource!!.listStory)
                    Log.d("arifwahyu", "koneksi sukses" )
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.d("error_getlist", t.message.toString())
            }

        })

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
