package com.rolandoamarillo.addi.network.response

import com.google.gson.annotations.SerializedName

/**
 * Response object for {@link Endpoints.PUBLIC_INFORMATION}
 */
data class PublicInformationResponse(
    @SerializedName("documentType") val documentType: String,
    @SerializedName("documentNumber") val documentNumber: String
)