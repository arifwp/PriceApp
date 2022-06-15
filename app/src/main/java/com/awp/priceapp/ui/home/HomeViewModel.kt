package com.awp.priceapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awp.priceapp.response.ListProduct

class HomeViewModel : ViewModel() {

    val listProduct = MutableLiveData<ArrayList<ListProduct>>()

}