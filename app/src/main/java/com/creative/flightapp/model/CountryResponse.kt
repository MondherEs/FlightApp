package com.creative.flightapp.model

class CountryResponse : ArrayList<CountryResponse.CountryResponseItem>(){
    data class CountryResponseItem(
        val id: String,
        val name: String
    )
}