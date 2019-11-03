package com.rolandoamarillo.addi.network.response

import com.google.gson.annotations.SerializedName

/**
 * Response object for {@link Endpoints.RATING_INFORMATION}
 */
data class ProspectRatingResponse(
    @SerializedName("documentType") val documentType: String,
    @SerializedName("documentNumber") val documentNumber: String,
    @SerializedName("rating") val rating: Int
)