package com.awp.priceapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.databinding.ActivityAddProductBinding
import com.awp.priceapp.databinding.ActivityPriceOptimizeBinding
import com.awp.priceapp.response.FileUploadResponse
import com.awp.priceapp.response.ProductResponse
import com.awp.priceapp.utils.rotateBitmap
import com.awp.priceapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductActivity : AppCompatActivity() {

    val product = arrayOf("Bottle", "Kemaja", "Celana", "Meja", "Kursi", "Meja Kerja", "Meja Belajar")

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!authorize()) {
                Toast.makeText(this, "Tidak Diizinkan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun authorize() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

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

        ifGranted()
        setBindButton()



    }

    private fun setBindButton() {
        binding.btnCamera.setOnClickListener { runningCameraX() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun runningCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun ifGranted() {
        if (!authorize()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun runningGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.imagePreview.setImageBitmap(result)
        }
    }

    private fun doingService() {
        val file = getFile as File

        val descStory = binding.descProduct
        val description = descStory.text.toString().toRequestBody("text/plain".toMediaType())
        val reqImgFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            reqImgFile
        )

//        val token = sharedpref.getToken(Constant.PREF_TOKEN)
//        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
//        val service = retIn.uploadImage("bearer $token", imageMultipart, description)

        val client = ApiConfig.getApiService().uploadImage(imageMultipart, description)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null && !response.status!!) {
                        Toast.makeText(this@AddProductActivity, response.message, Toast.LENGTH_SHORT).show()
                        moveActivity()
                    }
                } else {
                    Toast.makeText(this@AddProductActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })

//        service.enqueue(object : Callback<FileUploadResponse> {
//            override fun onResponse(
//                call: Call<FileUploadResponse>,
//                response: Response<FileUploadResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val response = response.body()
//                    if (response != null && !response.error!!) {
//                        Toast.makeText(this@AddProductActivity, response.message, Toast.LENGTH_SHORT).show()
//                        moveActivity()
//                    }
//                } else {
//                    Toast.makeText(this@AddProductActivity, response.message(), Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                Toast.makeText(this@AddProductActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddProductActivity)

            getFile = myFile

            binding.imagePreview.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            doingService()
        } else {
            Toast.makeText(this@AddProductActivity, "Please Input Your Picture Bro!", Toast.LENGTH_SHORT).show()
        }
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