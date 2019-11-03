package com.rolandoamarillo.addi.network

import com.rolandoamarillo.addi.network.response.JusticeInformationResponse
import com.rolandoamarillo.addi.network.response.ProspectRatingResponse
import com.rolandoamarillo.addi.network.response.PublicInformationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for calls related to {@link Prospect}
 */
interface ProspectApi {

    /**
     * Consumes endpoint to get the information from external republic information system
     */
    @GET(Endpoints.PUBLIC_INFORMATION)
    fun getPublicInformation(
        @Query("document_type") documentType: String,
        @Query("document") documentNumber: String
    ): Call<PublicInformationResponse>

    /**
     * Consumes endpoint to get the crimes reported information from external police system
     */
    @GET(Endpoints.JUSTICE_INFORMATION)
    fun getJusticeInformation(
        @Query("document_type") documentType: String,
        @Query("document") documentNumber: String
    ): Call<JusticeInformationResponse>

    /**
     * Consumes endpoint to get the rating information from internal rating system
     */
    @GET(Endpoints.RATING_INFORMATION)
    fun getRating(
        @Query("document_type") documentType: String,
        @Query("document") documentNumber: String
    ): Call<ProspectRatingResponse>

}