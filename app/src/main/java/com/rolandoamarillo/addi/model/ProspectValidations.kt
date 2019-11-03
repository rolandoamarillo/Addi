package com.rolandoamarillo.addi.model

import androidx.annotation.StringRes
import com.rolandoamarillo.addi.R

/**
 * Enum to represent validation results related to prospect and the messages assigned
 */
enum class ProspectValidations(@StringRes val message: Int) {

    OK(R.string.empty),
    EMPTY_DOC_TYPE(R.string.empty_document_type),
    EMPTY_DOC_NUMBER(R.string.empty_document_number),
    USER_ALREADY_EXISTS(R.string.user_already_exists),
    EMPTY_DOC_EXPIRATION_DATE(R.string.empty_document_expedition_date),
    EMPTY_NAME(R.string.empty_name),
    EMPTY_LASTNAME(R.string.empty_lastname),
    EMPTY_PHONE(R.string.empty_phone),
    INVALID_PHONE(R.string.invalid_phone),
    EMPTY_EMAIL(R.string.empty_email),
    INVALID_EMAIL(R.string.invalid_email),

    DOCUMENT_NOT_FOUND(R.string.document_not_found),
    DOCUMENT_WITH_CRIMES(R.string.document_with_crimes),
    DOCUMENT_LOW_RATING(R.string.document_low_rating)
}