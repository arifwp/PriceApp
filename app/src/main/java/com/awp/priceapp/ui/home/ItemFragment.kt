package com.awp.priceapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.awp.priceapp.adapter.ProductsAdapter
import com.awp.priceapp.databinding.FragmentItemBinding

class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsAdapter
    private lateinit var itemViewModel: ItemViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductsAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvItem.setHasFixedSize(true)
            rvItem.layoutManager = LinearLayoutManager(activity)
            rvItem.adapter = adapter
        }

        itemViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ItemViewModel::class.java)
        itemViewModel.setListProducts()
        itemViewModel.getListProducts().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
            }
        }

    }



}
