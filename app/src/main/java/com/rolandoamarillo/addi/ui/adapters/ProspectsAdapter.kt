package com.rolandoamarillo.addi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rolandoamarillo.addi.R
import com.rolandoamarillo.addi.model.Prospect
import kotlinx.android.synthetic.main.prospect_view.view.*

/**
 * Adapter to show prospects on {@link RecyclerView}
 */
class ProspectsAdapter(private val listener: (Prospect) -> Unit) :
    RecyclerView.Adapter<ProspectsAdapter.ProspectViewHolder>() {

    /**
     * Internal prospects list
     */
    private val prospects = ArrayList<Prospect>()

    /**
     * ViewHolder to manage prospects
     */
    class ProspectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Binds a view with the prospect associated to the ViewHolder
         */
        fun bind(prospect: Prospect, listener: (Prospect) -> Unit) = with(itemView) {
            val name = prospect.name + " " + prospect.lastname
            nameTextView.text = name
            phoneTextView.text = prospect.phone
            emailTextView.text = prospect.email
            documentTypeTextView.text = prospect.docType
            documentExpirationDateTextView.text = prospect.docExpedition
            documentNumberTextView.text = prospect.docNumber
            validateButton.visibility = if (prospect.contact) View.GONE else View.VISIBLE
            validateButton.setOnClickListener { listener(prospect) }
            setOnClickListener { listener(prospect) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProspectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prospect_view, parent, false)
        return ProspectViewHolder(view)
    }

    override fun getItemCount(): Int = prospects.size

    override fun onBindViewHolder(holder: ProspectViewHolder, position: Int) =
        holder.bind(prospects[position], listener)

    /**
     * Sets the prospects to show and notifies the changes
     */
    fun setProspects(prospectList: List<Prospect>) {
        prospects.clear()
        prospects.addAll(prospectList)
        notifyDataSetChanged()
    }
}