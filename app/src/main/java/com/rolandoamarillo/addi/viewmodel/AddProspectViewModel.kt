package com.rolandoamarillo.addi.viewmodel

import android.os.AsyncTask
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.model.ProspectValidations
import com.rolandoamarillo.addi.repository.ProspectDataSource

/**
 * ViewModel for the add prospect view.
 */
class AddProspectViewModel(private val prospectDataSource: ProspectDataSource) : ViewModel() {

    private val _addedProspect = MutableLiveData<Prospect>()

    /**
     * LiveData to observe for added prospects
     */
    val addedProspect: LiveData<Prospect> get() = _addedProspect

    private val _prospectValidations = MutableLiveData<Set<ProspectValidations>>()

    /**
     * LiveData to observe for prospect local validations
     */
    val prospectValidations: LiveData<Set<ProspectValidations>> get() = _prospectValidations

    /**
     * Adds a prospect to the database if all validations are correct.
     */
    fun addProspect(
        documentType: String,
        documentNumber: String,
        documentExpeditionDate: String,
        name: String,
        lastName: String,
        phone: String,
        email: String
    ) {

        AsyncTask.execute {
            val prospectValidations = HashSet<ProspectValidations>()

            prospectValidations.addAll(
                setOf(
                    isDocumentTypeValid(documentType),
                    isDocumentNumberValid(documentNumber),
                    isUserAlreadyAdded(documentType, documentNumber),
                    isDocumentExpirationDateValid(documentExpeditionDate),
                    isNameValid(name),
                    isLastNameValid(lastName),
                    isPhoneValid(phone),
                    isEmailValid(email)
                )
            )

            _prospectValidations.postValue(prospectValidations)

            if (prospectValidations.all { it == ProspectValidations.OK }) {
                val prospect = Prospect(
                    documentType,
                    documentNumber,
                    documentExpeditionDate,
                    name,
                    lastName,
                    phone,
                    email
                )

                prospectDataSource.addProspect(prospect)
                _addedProspect.postValue(prospect)
            }
        }
    }

    /**
     * Validates the document type input
     */
    private fun isDocumentTypeValid(documentType: String): ProspectValidations {
        if (TextUtils.isEmpty(documentType)) {
            return ProspectValidations.EMPTY_DOC_TYPE
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the document number input
     */
    private fun isDocumentNumberValid(documentNumber: String): ProspectValidations {
        if (TextUtils.isEmpty(documentNumber)) {
            return ProspectValidations.EMPTY_DOC_NUMBER
        }
        return ProspectValidations.OK
    }

    /**
     * Validates if the user already exists on the local database
     */
    private fun isUserAlreadyAdded(
        documentType: String,
        documentNumber: String
    ): ProspectValidations {
        if (isDocumentTypeValid(documentType) == ProspectValidations.OK
            && isDocumentNumberValid(documentNumber) == ProspectValidations.OK
            && prospectDataSource.getProspect(documentType, documentNumber) != null
        ) {
            return ProspectValidations.USER_ALREADY_EXISTS
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the document expiration date
     */
    private fun isDocumentExpirationDateValid(documentExpeditionDate: String): ProspectValidations {
        if (TextUtils.isEmpty(documentExpeditionDate)) {
            return ProspectValidations.EMPTY_DOC_EXPIRATION_DATE
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the name
     */
    private fun isNameValid(name: String): ProspectValidations {
        if (TextUtils.isEmpty(name)) {
            return ProspectValidations.EMPTY_NAME
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the last name
     */
    private fun isLastNameValid(lastName: String): ProspectValidations {
        if (TextUtils.isEmpty(lastName)) {
            return ProspectValidations.EMPTY_LASTNAME
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the phone
     */
    private fun isPhoneValid(phone: String): ProspectValidations {
        if (TextUtils.isEmpty(phone)) {
            return ProspectValidations.EMPTY_PHONE
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            return ProspectValidations.INVALID_PHONE
        }
        return ProspectValidations.OK
    }

    /**
     * Validates the email
     */
    private fun isEmailValid(email: String): ProspectValidations {
        if (TextUtils.isEmpty(email)) {
            return ProspectValidations.EMPTY_EMAIL
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ProspectValidations.INVALID_EMAIL
        }
        return ProspectValidations.OK
    }

}
