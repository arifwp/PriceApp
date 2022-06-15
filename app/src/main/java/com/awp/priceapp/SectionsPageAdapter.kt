package com.awp.priceapp

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.awp.priceapp.ui.home.CategoryFragment
import com.awp.priceapp.ui.home.HomeFragment
import com.awp.priceapp.ui.home.ItemFragment

class SectionsPageAdapter(private val mContext: HomeFragment, fragment: FragmentManager, data: Bundle): FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.item,
        R.string.category
    )

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = ItemFragment()
            1 -> fragment = CategoryFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }


}