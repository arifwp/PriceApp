package com.awp.priceapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.awp.priceapp.R
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.body.UploadBody
import com.awp.priceapp.databinding.ActivityAddProductBinding
import com.awp.priceapp.response.FileUploadResponse
import com.awp.priceapp.response.GetNameResponse
import com.awp.priceapp.response.listName
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class AddProductActivity : AppCompatActivity() {

    var product = listOf("Botol", "Kemaja", "Celana", "Meja", "Kursi", "Meja Kerja", "Meja Belajar")

    private lateinit var binding: ActivityAddProductBinding

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: Button
    lateinit var btn_upload_image: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()


        val client = ApiConfig.getApiService().searchName()

        client.enqueue(object : Callback<GetNameResponse> {
            override fun onResponse(
                call: Call<GetNameResponse>,
                response: Response<GetNameResponse>
            ) {
                if (response.isSuccessful) {
                    val resource = response.body()
                    if (response.body() != null) {
                        Toast.makeText(this@AddProductActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        val addProduct = response.body()!!.data_product.map { it.ProductName!! }


                        val newData  = response.body()!!.data_product
                        Collections.replaceAll(product, "Botol", newData.toString())

//                        Log.e("isi_list", response.body()!!.data_product.toString())


                        val autocomp = binding.autoComplete
                        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, addProduct)
                        autocomp.setAdapter(adapterArray)
                        autocomp.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                            Toast.makeText(applicationContext, "clicked item = " + addProduct[position], Toast.LENGTH_SHORT).show()
                        })


                        Log.e("isi_list", newData.toString())
                    }
                } else {
                    Toast.makeText(this@AddProductActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GetNameResponse>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })

//        val autocomp = binding.autoComplete
//        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, product)
//        autocomp.setAdapter(adapterArray)
//        autocomp.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            Toast.makeText(applicationContext, "clicked item = " + product[position], Toast.LENGTH_SHORT).show()
//        })

        showHidePriceOptimizer()

        btn_choose_image = findViewById(R.id.btn_choose_image)
        imagePreview = findViewById(R.id.image_preview)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btn_choose_image.setOnClickListener { launchGallery() }



        binding.btnUpload.setOnClickListener {

            if(filePath != null){
                val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
                val uploadTask = ref?.putFile(filePath!!)
                val fileName = "$ref.jpeg"

                addProduct(
                    binding.autoComplete.text.toString(),
                    binding.categoryField.text.toString(),
                    binding.priceProduct.text.toString().toInt(),
                    binding.stockField.text.toString().toInt(),
                    binding.modalField.text.toString().toInt(),
                    binding.descField.text.toString(),
                    fileName.toString()
                )

            }else{
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }

        }


    }


    private fun addProduct(name: String, category: String, price: Int, quantity: Int, cost: Int, description: String, photos: String){

        val productInfo = UploadBody(name, category, price, quantity, cost, description, photos)
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

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/jpeg"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}