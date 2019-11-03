package com.rolandoamarillo.addi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.ui.adapters.ProspectsAdapter
import com.rolandoamarillo.addi.ui.fragments.base.BaseFragment
import com.rolandoamarillo.addi.viewmodel.AgendaViewModel
import kotlinx.android.synthetic.main.agenda_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that shows the list of contacts
 */
class AgendaFragment : BaseFragment() {

    companion object {
        /**
         * Creates a new instance of the fragment
         */
        fun newInstance() = AgendaFragment()
    }

    /**
     * ViewModel
     */
    private val viewModel: AgendaViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agenda_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(context)
        val prospectsAdapter = ProspectsAdapter {}

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = prospectsAdapter
        }


        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            prospectsAdapter.setProspects(it)
        })
    }

}
