package com.awp.priceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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

        showHidePriceOptimizer()



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