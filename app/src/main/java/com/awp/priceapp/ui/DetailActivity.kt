package com.awp.priceapp.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.databinding.ActivityDetailBinding
import com.awp.priceapp.response.ListProduct
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.hide()

        val story = intent.getParcelableExtra<ListProduct>(EXTRA_DETAIL)
        parsingData(story)

        binding.btnDelete.setOnClickListener {
            val itemData = intent.getParcelableExtra<ListProduct>(EXTRA_DETAIL)
            deleteItem(itemData)
        }

    }

    private fun deleteItem(itemData: ListProduct?) {

        val itemId = itemData?.id
        val client = itemId?.let { ApiConfig.getApiService().deleteData(it) }
        if (client != null) {
            client.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response != null) {
                        Toast.makeText(this@DetailActivity, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                        Log.d("success_delete", response.code().toString())
                        moveActivity()
                    } else {
                        Toast.makeText(this@DetailActivity, response.message(), Toast.LENGTH_SHORT).show()
                        Log.e("gagal_ditambahkan", response.toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("cant_delete", t.message.toString())
                }

            })
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun parsingData(listProduct: ListProduct?) {
        if (listProduct != null) {
            binding.apply {
                tvProductName.text = listProduct.ProductName
                tvProductPrice.text = listProduct.Price.toString()
                tvStock.text = listProduct.Quantity.toString()
                tvDescription.text = listProduct.Description
                tvCategory.text = listProduct.ProductCategory
                Glide
                    .with(this@DetailActivity)
                    .load(listProduct.Photos)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(tvImgDetail)
            }
        }
    }
}