package com.creative.flightapp.apiservice

import com.creative.flightapp.model.CountryResponse
import com.creative.flightapp.model.FlightDetailResponse
import com.creative.flightapp.model.FlightResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FlightApi {

    @GET("v1/airoport")
    suspend fun getAeroport(): Response<CountryResponse>

    @GET("v1/flight")
    suspend fun getFlight(): Response<FlightResponse>

    @GET("v1/flight/{number}")
    suspend fun getFlightDetail(@Path("number") number: Int): Response<FlightDetailResponse>
}