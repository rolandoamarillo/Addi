package com.rolandoamarillo.addi.viewmodel

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.model.ProspectValidations
import com.rolandoamarillo.addi.network.response.JusticeInformationResponse
import com.rolandoamarillo.addi.network.response.ProspectRatingResponse
import com.rolandoamarillo.addi.network.response.PublicInformationResponse
import com.rolandoamarillo.addi.repository.ProspectDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProspectsViewModel(private val prospectDataSource: ProspectDataSource) : ViewModel() {

    companion object {
        private val TAG = ProspectsViewModel::class.java.simpleName
    }

    val prospects get() = prospectDataSource.prospects

    private val _prospectsRemoteValidations = MutableLiveData<Set<ProspectValidations>>()

    val prospectsRemoteValidations: LiveData<Set<ProspectValidations>> get() = _prospectsRemoteValidations

    private val _prospectAdded = MutableLiveData<Prospect>()

    val prospectAdded: LiveData<Prospect> get() = _prospectAdded

    fun addContact(prospect: Prospect) {
        AsyncTask.execute {
            validateProspect(prospect)
        }
    }

    private fun validateProspect(prospect: Prospect) {
        var publicInformationCompleted = false
        var justiceInformationCompleted = false
        var publicInformationResponse: PublicInformationResponse? = null
        var justiceInformationResponse: JusticeInformationResponse? = null

        prospectDataSource
            .getPublicInformation(prospect.docType, prospect.docNumber)
            .enqueue(object : Callback<PublicInformationResponse> {

                override fun onFailure(call: Call<PublicInformationResponse>, t: Throwable) {
                    Log.e(TAG, "Error Getting Public Info", t)
                    publicInformationCompleted = true
                    justiceInformationCompleted = true
                    onCallbackCompleted(
                        prospect,
                        publicInformationResponse,
                        justiceInformationResponse
                    )
                }

                override fun onResponse(
                    call: Call<PublicInformationResponse>,
                    response: Response<PublicInformationResponse>
                ) {
                    if (!publicInformationCompleted) {
                        publicInformationCompleted = true
                        publicInformationResponse = response.body()
                        if (justiceInformationCompleted) {
                            onCallbackCompleted(
                                prospect,
                                publicInformationResponse,
                                justiceInformationResponse
                            )
                        }
                    }
                }

            })

        prospectDataSource.getJusticeInformation(prospect.docType, prospect.docNumber)
            .enqueue(object : Callback<JusticeInformationResponse> {
                override fun onFailure(call: Call<JusticeInformationResponse>, t: Throwable) {
                    Log.e(TAG, "Error Getting Justice Info", t)
                    publicInformationCompleted = true
                    justiceInformationCompleted = true
                    onCallbackCompleted(
                        prospect, publicInformationResponse, justiceInformationResponse
                    )
                }

                override fun onResponse(
                    call: Call<JusticeInformationResponse>,
                    response: Response<JusticeInformationResponse>
                ) {
                    if (!justiceInformationCompleted) {
                        justiceInformationCompleted = true
                        justiceInformationResponse = response.body()
                        if (publicInformationCompleted) {
                            onCallbackCompleted(
                                prospect,
                                publicInformationResponse,
                                justiceInformationResponse
                            )
                        }
                    }
                }
            })
    }

    private fun onCallbackCompleted(
        prospect: Prospect,
        publicInformationResponse: PublicInformationResponse?,
        justiceInformationResponse: JusticeInformationResponse?
    ) {

        val prospectValidationsArray = setOf(
            validateDocument(prospect, publicInformationResponse),
            validateJustice(prospect, justiceInformationResponse)
        )

        _prospectsRemoteValidations.postValue(prospectValidationsArray)
        if (prospectValidationsArray.all { it == ProspectValidations.OK }) {
            onCallbackSuccess(prospect)
        }
    }

    private fun validateDocument(
        prospect: Prospect,
        publicInformationResponse: PublicInformationResponse?
    ): ProspectValidations {
        if (publicInformationResponse == null
            || publicInformationResponse.documentType != prospect.docType
            || publicInformationResponse.documentNumber != prospect.docNumber
        ) {
            return ProspectValidations.DOCUMENT_NOT_FOUND
        }
        return ProspectValidations.OK
    }

    private fun validateJustice(
        prospect: Prospect,
        justiceInformationResponse: JusticeInformationResponse?
    ): ProspectValidations {
        if (justiceInformationResponse == null || justiceInformationResponse.documentType
            != prospect.docType || justiceInformationResponse.documentNumber != prospect.docNumber
        ) {
            return ProspectValidations.DOCUMENT_NOT_FOUND
        } else if (justiceInformationResponse.reportedCrimes >= 1) {
            return ProspectValidations.DOCUMENT_WITH_CRIMES
        }
        return ProspectValidations.OK
    }

    private fun onCallbackSuccess(
        prospect: Prospect
    ) {
        prospectDataSource.getRating(prospect.docType, prospect.docNumber)
            .enqueue(object : Callback<ProspectRatingResponse> {
                override fun onFailure(call: Call<ProspectRatingResponse>, t: Throwable) {
                    Log.e(TAG, "Error Getting Rating Info", t)
                    _prospectsRemoteValidations.postValue(setOf(ProspectValidations.DOCUMENT_NOT_FOUND))
                }

                override fun onResponse(
                    call: Call<ProspectRatingResponse>,
                    response: Response<ProspectRatingResponse>
                ) {
                    val prospectRating = response.body()
                    val validations = validateRating(prospect, prospectRating)
                    if (validations == ProspectValidations.OK) {
                        prospectDataSource.addContact(prospect)
                        _prospectAdded.postValue(prospect)
                    } else {
                        _prospectsRemoteValidations.postValue(setOf(validations))
                    }
                }
            })
    }

    private fun validateRating(
        prospect: Prospect,
        prospectRatingResponse: ProspectRatingResponse?
    ): ProspectValidations {
        return if (prospectRatingResponse == null
            || prospectRatingResponse.documentType != prospect.docType
            || prospectRatingResponse.documentNumber != prospect.docNumber
        ) {
            ProspectValidations.DOCUMENT_NOT_FOUND
        } else if (prospectRatingResponse.rating < 60) ProspectValidations.DOCUMENT_LOW_RATING
        else ProspectValidations.OK
    }
}
