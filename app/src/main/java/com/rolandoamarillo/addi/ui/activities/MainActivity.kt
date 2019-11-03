package com.rolandoamarillo.addi.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.ui.activities.base.BaseActivity
import com.rolandoamarillo.addi.ui.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main activity that shows the list of contacts and the list of prospects
 */
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        fab.setOnClickListener {
            startActivityForResult(
                AddProspectActivity.newIntent(this),
                AddProspectActivity.REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddProspectActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Snackbar.make(viewPager, getString(R.string.prospect_added), Snackbar.LENGTH_LONG)
                .show()
        }
    }
}