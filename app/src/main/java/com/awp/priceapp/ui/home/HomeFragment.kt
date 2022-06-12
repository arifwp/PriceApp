package com.awp.priceapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.awp.priceapp.AddProductActivity
import com.awp.priceapp.R
import com.awp.priceapp.SectionsPageAdapter
import com.awp.priceapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var fragmentHome: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val binding = FragmentHomeBinding.bind(view)
        fragmentHome = binding
        val bundle = Bundle()

        val sectionsPageAdapter = SectionsPageAdapter(this, childFragmentManager, bundle)
        binding.apply{
            viewPager.adapter = sectionsPageAdapter
            tabs.setupWithViewPager(viewPager)
        }

        binding.fab.setOnClickListener {
            val intent = Intent (getActivity(), AddProductActivity::class.java)
            getActivity()?.startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}