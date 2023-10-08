package com.creative.flightapp.repositories

import com.creative.flightapp.apiservice.FlightApi
import com.creative.flightapp.model.CountryResponse
import com.creative.flightapp.model.FlightDetailResponse
import com.creative.flightapp.model.FlightResponse
import retrofit2.Response
import javax.inject.Inject


class MainRepository @Inject constructor(private val mainAPICall: FlightApi) {

    suspend fun getCountry(): Response<CountryResponse> {
        return mainAPICall.getAeroport()
    }
    suspend fun getFlight(): Response<FlightResponse> {
        return mainAPICall.getFlight()
    }

    suspend fun getFlightDetail(number: Int): Response<FlightDetailResponse> {
        return mainAPICall.getFlightDetail(number)
    }

}