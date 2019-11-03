package com.rolandoamarillo.addi

import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.network.response.JusticeInformationResponse
import com.rolandoamarillo.addi.network.response.ProspectRatingResponse
import com.rolandoamarillo.addi.network.response.PublicInformationResponse

class TestUtil {

    companion object {
        fun createProspect(docType: String, docNumber: String): Prospect {
            return Prospect(docType, docNumber, null, null, null, null, null, false)
        }

        fun createPublicInformationResponse(
            docType: String,
            docNumber: String
        ): PublicInformationResponse {
            return PublicInformationResponse(docType, docNumber)
        }

        fun createJusticeInformationResponse(
            docType: String,
            docNumber: String,
            reportedCrimes: Int
        ): JusticeInformationResponse {
            return JusticeInformationResponse(docType, docNumber, reportedCrimes)
        }

        fun createRatingInformationResponse(
            docType: String,
            docNumber: String,
            rating: Int
        ): ProspectRatingResponse {
            return ProspectRatingResponse(docType, docNumber, rating)
        }
    }

}