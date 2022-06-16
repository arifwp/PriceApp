package com.awp.priceapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_DATA
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awp.priceapp.databinding.LayoutItemBinding
import com.awp.priceapp.response.ListProduct
import com.awp.priceapp.response.ProductResponse
import com.awp.priceapp.ui.DetailActivity
import com.awp.priceapp.ui.DetailActivity.Companion.EXTRA_DETAIL
import com.bumptech.glide.Glide

class ProductsAdapter : ListAdapter<ListProduct, ProductsAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(holder.itemView.context, story)
    }


    class ViewHolder(private val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, listProduct: ListProduct) {
            binding.apply {

                nameItem.text = listProduct.ProductName
                priceItem.text = listProduct.Price.toString()
                Glide
                    .with(context)
                    .load(listProduct.Photos)
                    .into(imgItem)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        root.context as Activity,
                        Pair(imgItem, "img_item"),
                        Pair(nameItem, "name_item"),
                        Pair(priceItem, "price")
                    )

                    Intent(context, DetailActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_DETAIL, listProduct)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListProduct>() {
            override fun areItemsTheSame(oldItem: ListProduct, newItem: ListProduct): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListProduct, newItem: ListProduct): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}