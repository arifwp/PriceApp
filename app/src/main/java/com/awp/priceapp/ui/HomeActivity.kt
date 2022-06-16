package com.awp.priceapp.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awp.priceapp.R
import com.awp.priceapp.adapter.ProductsAdapter
import com.awp.priceapp.api.ApiConfig
import com.awp.priceapp.databinding.ActivityHomeBinding
import com.awp.priceapp.response.ListProduct
import com.awp.priceapp.response.ProductResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var recyclerView: RecyclerView

    private val _list = MutableLiveData<ProductResponse>()
    val list: LiveData<ProductResponse> = _list

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productsAdapter = ProductsAdapter()
        productsAdapter.notifyDataSetChanged()
        getSupportActionBar()?.hide()


        setRv()

        // call requestIdToken as follows
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        val client = ApiConfig.getApiService().getAllPost()
        client.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful){
                    val resource: ProductResponse? = response.body()
                    updatingData(resource!!.data)
                    Log.d("sambungan", response.body().toString())
                } else {
                    Log.d("sambunganelse", response.message())
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.d("error_getlist", t.message.toString())
            }

        })

        getUserInformation()

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getUserInformation() {
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl

            binding.apply {
                tvDisplayName.text = personName
                tvEmailAccount.text = personEmail
                Glide
                    .with(this@HomeActivity)
                    .load(personPhoto)
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
                    .into(profileImg)
            }
        }
    }

    private fun setRv() {
        val linearLayoutManager = LinearLayoutManager(this)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    val intent= Intent(this, LoginActivity::class.java)
                    Toast.makeText(this,"Logging Out",Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}