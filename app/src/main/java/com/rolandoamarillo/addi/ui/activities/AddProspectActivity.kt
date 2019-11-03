package com.rolandoamarillo.addi.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.ui.activities.base.BaseActivity
import com.rolandoamarillo.addi.ui.fragments.AddProspectFragment

/**
 * Activity to add prospects
 */
class AddProspectActivity : BaseActivity(), AddProspectFragment.OnProspectAddedListener {

    companion object {

        const val REQUEST_CODE = 3948

        fun newIntent(context: Context): Intent {
            return Intent(context, AddProspectActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_prospect_activity)
        if (savedInstanceState == null) {

            val fragment = AddProspectFragment.newInstance(this)

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    override fun onProspectAdded(prospect: Prospect) {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
