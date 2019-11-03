package com.rolandoamarillo.addi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.model.ProspectValidations
import com.rolandoamarillo.addi.ui.adapters.ProspectsAdapter
import com.rolandoamarillo.addi.ui.fragments.base.BaseFragment
import com.rolandoamarillo.addi.viewmodel.ProspectsViewModel
import kotlinx.android.synthetic.main.prospects_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that shows the list of prospects
 */
class ProspectsFragment : BaseFragment() {

    companion object {
        /**
         * Creates a new instance of the fragment
         */
        fun newInstance() = ProspectsFragment()
    }

    /**
     * ViewModel
     */
    private val viewModel: ProspectsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.prospects_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(context)
        val prospectsAdapter = ProspectsAdapter {
            progressView.visibility = View.VISIBLE
            viewModel.addContact(it)
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = prospectsAdapter
        }


        viewModel.prospects.observe(viewLifecycleOwner, Observer {
            prospectsAdapter.setProspects(it)
        })

        viewModel.prospectsRemoteValidations.observe(viewLifecycleOwner, Observer {
            if (!it.all { validation -> validation == ProspectValidations.OK }) {
                progressView.visibility = View.INVISIBLE
                val stringBuilder = StringBuilder()
                for (validation in it) {
                    if (validation != ProspectValidations.OK) {
                        stringBuilder.append(getString(validation.message))
                    }
                }

                Snackbar.make(rootView, stringBuilder, Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.prospectAdded.observe(viewLifecycleOwner, Observer {
            progressView.visibility = View.INVISIBLE
            Snackbar.make(rootView, getString(R.string.contact_added), Snackbar.LENGTH_LONG).show()
        })
    }

}
