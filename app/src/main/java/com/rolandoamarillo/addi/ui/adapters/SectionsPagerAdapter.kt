package com.rolandoamarillo.addi.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.ui.fragments.AgendaFragment
import com.rolandoamarillo.addi.ui.fragments.ProspectsFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {

        /**
         * Titles for the tabs
         */
        private val TAB_TITLES = arrayOf(
            R.string.tab_agenda,
            R.string.tab_prospects
        )
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) AgendaFragment.newInstance()
        else ProspectsFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}