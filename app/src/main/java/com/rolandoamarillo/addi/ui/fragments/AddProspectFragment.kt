package com.rolandoamarillo.addi.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.Observer
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.model.ProspectValidations
import com.rolandoamarillo.addi.ui.fragments.base.BaseFragment
import com.rolandoamarillo.addi.viewmodel.AddProspectViewModel
import kotlinx.android.synthetic.main.add_prospect_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that shows a form to add a prospect
 */
class AddProspectFragment : BaseFragment() {

    companion object {

        /**
         * Creates a new instance based on the params provided
         */
        fun newInstance(listener: OnProspectAddedListener): AddProspectFragment {
            val fragment = AddProspectFragment()
            fragment.listener = listener
            return fragment
        }
    }

    /**
     * Listener that will be triggered on prospect add
     */
    private var listener: OnProspectAddedListener? = null

    /**
     * ViewModel
     */
    private val viewModel: AddProspectViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.add_prospect_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        initObservables()
    }

    /**
     * Inits the UI
     */
    private fun initUI() {
        val documentTypes: Array<String> = resources.getStringArray(R.array.document_type)

        spinner.adapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            documentTypes
        )

        date.setOnClickListener {
            showExpeditionDatePickerDialog()
        }

        date.setOnFocusChangeListener { _, focused ->
            if (focused) {
                showExpeditionDatePickerDialog()
            }
        }

        addButton.setOnClickListener {
            val documentType = spinner.selectedItem.toString()
            val documentNumberText = documentNumber.text.toString()
            val dateText = date.text.toString()
            val nameText = name.text.toString()
            val lastNameText = lastname.text.toString()
            val phoneText = phone.text.toString()
            val emailText = email.text.toString()

            progressView.visibility = View.VISIBLE

            viewModel.addProspect(
                documentType,
                documentNumberText,
                dateText,
                nameText,
                lastNameText,
                phoneText,
                emailText
            )
        }
    }

    /**
     * Inits all the observables needed
     */
    private fun initObservables() {

        viewModel.addedProspect.observe(viewLifecycleOwner, Observer {
            progressView.visibility = View.GONE
            listener?.onProspectAdded(it)
        })

        viewModel.prospectValidations.observe(viewLifecycleOwner, Observer {
            documentNumberInputLayout.error = null
            dateInputLayout.error = null
            nameInputLayout.error = null
            lastNameInputLayout.error = null
            phone.error = null
            email.error = null

            if (!it.all { validation -> validation == ProspectValidations.OK }) {
                progressView.visibility = View.GONE
            }

            for (validation in it) {
                val message = getString(validation.message)

                when (validation) {
                    ProspectValidations.EMPTY_DOC_NUMBER,
                    ProspectValidations.USER_ALREADY_EXISTS -> documentNumberInputLayout.error =
                        message
                    ProspectValidations.EMPTY_NAME -> nameInputLayout.error = message
                    ProspectValidations.EMPTY_DOC_EXPIRATION_DATE -> dateInputLayout.error = message
                    ProspectValidations.EMPTY_LASTNAME -> lastNameInputLayout.error = message
                    ProspectValidations.EMPTY_PHONE -> phoneInputLayout.error = message
                    ProspectValidations.INVALID_PHONE -> phoneInputLayout.error = message
                    ProspectValidations.EMPTY_EMAIL -> emailInputLayout.error = message
                    ProspectValidations.INVALID_EMAIL -> emailInputLayout.error = message
                    else -> {
                        //Do nothing
                    }
                }
            }
        })

    }

    /**
     * Shows the dialog to select the document expedition date
     */
    private fun showExpeditionDatePickerDialog() {
        val newFragment =
            DatePickerFragment.newInstance(object : DatePickerFragment.DatePickerListener {
                override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
                    val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
                    date.setText(selectedDate)
                    name.requestFocus()
                }

                override fun onDismiss(p0: DialogInterface?) {
                    name.requestFocus()
                }

                override fun onCancel(p0: DialogInterface?) {
                    name.requestFocus()
                }

            })
        newFragment.show(childFragmentManager, "date_picker")
    }

    /**
     * Interface for listening when a prospect is added from this fragment
     */
    interface OnProspectAddedListener {

        /**
         * Called when a prospect is added
         */
        fun onProspectAdded(prospect: Prospect)
    }
}
