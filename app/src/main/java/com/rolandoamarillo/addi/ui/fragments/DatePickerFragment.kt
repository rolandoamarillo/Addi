package com.rolandoamarillo.addi.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * DialogFragment to show a date picker
 */
class DatePickerFragment : DialogFragment() {

    /**
     * DatePickerListener to listen calendar events
     */
    private var listener: DatePickerListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), listener, year, month, day)
        dialog.setOnCancelListener(listener)
        dialog.setOnDismissListener(listener)

        return dialog
    }

    companion object {
        /**
         * Creates a new instance of the fragment with the params provided
         */
        fun newInstance(listener: DatePickerListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener?.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDismiss(dialog)
    }

    /**
     * Interface to listen to dialog events
     */
    interface DatePickerListener : DatePickerDialog.OnDateSetListener,
        DialogInterface.OnDismissListener, DialogInterface.OnCancelListener
}
