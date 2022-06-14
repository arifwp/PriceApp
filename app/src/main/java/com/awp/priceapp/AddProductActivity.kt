package com.awp.priceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.awp.priceapp.databinding.ActivityAddProductBinding
import com.awp.priceapp.databinding.ActivityPriceOptimizeBinding

class AddProductActivity : AppCompatActivity() {

    val product = arrayOf("Bottle", "Kemaja", "Celana", "Meja", "Kursi", "Meja Kerja", "Meja Belajar")

    private lateinit var binding: ActivityAddProductBinding

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

    }
}