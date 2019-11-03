package com.rolandoamarillo.addi.network.response

import com.google.gson.annotations.SerializedName

/**
 * Response object for {@link Endpoints.JUSTICE_INFORMATION}
 */
data class JusticeInformationResponse(
    @SerializedName("documentType") val documentType: String,
    @SerializedName("documentNumber") val documentNumber: String,
    @SerializedName("reportedCrimes") val reportedCrimes: Int
)