package com.rolandoamarillo.addi.repository

import androidx.lifecycle.LiveData
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.network.response.JusticeInformationResponse
import com.rolandoamarillo.addi.network.response.ProspectRatingResponse
import com.rolandoamarillo.addi.network.response.PublicInformationResponse
import retrofit2.Call

interface ProspectDataSource {

    /**
     * Prospects from the database
     */
    val prospects: LiveData<List<Prospect>>

    /**
     * Contacts from the database
     */
    val contacts: LiveData<List<Prospect>>

    /**
     * Get the information from external republic information system
     */
    fun getPublicInformation(
        documentType: String,
        documentNumber: String
    ): Call<PublicInformationResponse>

    /**
     * Get the crimes reported information from external police system
     */
    fun getJusticeInformation(
        documentType: String,
        documentNumber: String
    ): Call<JusticeInformationResponse>

    /**
     * Get the rating information from internal rating system
     */
    fun getRating(documentType: String, documentNumber: String): Call<ProspectRatingResponse>

    /**
     * Gets the prospect if exists based on the document type and number
     */
    fun getProspect(documentType: String, documentNumber: String): Prospect?

    /**
     * Adds a prospect to the database
     */
    fun addProspect(prospect: Prospect)

    /**
     * Upgrades the prospect as a contact and update it on the database
     */
    fun addContact(prospect: Prospect)

}