package com.awp.priceapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.body.UploadBody
import com.awp.priceapp.databinding.ActivityAddProductBinding
import com.awp.priceapp.response.FileUploadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductActivity : AppCompatActivity() {

    val product = arrayOf("Botol", "Kemaja", "Celana", "Meja", "Kursi", "Meja Kerja", "Meja Belajar")

    private lateinit var binding: ActivityAddProductBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()

        val autocomp = binding.autoComplete
        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, product)
        autocomp.setAdapter(adapterArray)
        autocomp.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(applicationContext, "clicked item = " + product[position], Toast.LENGTH_SHORT).show()
        })

        showHidePriceOptimizer()


        binding.btnUpload.setOnClickListener {

            addProduct(
                binding.autoComplete.text.toString(),
                binding.categoryField.text.toString(),
                binding.priceProduct.text.toString().toInt(),
                binding.stockField.text.toString().toInt(),
                binding.modalField.text.toString().toInt(),
                binding.descField.text.toString()
            )

        }


    }

    private fun addProduct(name: String, category: String, price: Int, quantity: Int, cost: Int, description: String){

        val productInfo = UploadBody(name, category, price, quantity, cost, description)
        val client = ApiConfig.getApiService().uploadImage(productInfo)

        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null && !response.status!!) {
                        Toast.makeText(this@AddProductActivity, response.message, Toast.LENGTH_SHORT).show()
                        Log.e("product_ditambahkan", response.toString())
                        moveActivity()
                    }
                } else {
                    Toast.makeText(this@AddProductActivity, response.message(), Toast.LENGTH_SHORT).show()
                    Log.e("gagal_ditambahkan", response.toString())
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                Log.e("gagal_instance", t.message.toString())
            }

        })
    }


    private fun moveActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showHidePriceOptimizer() {

        val modal = binding.modalField
        val relative = binding.relativeBox

        if(modal.text.isEmpty()) {
            relative.visibility = View.GONE
        }
        modal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                relative.visibility = View.VISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

}