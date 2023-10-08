package com.creative.flightapp.model

class FlightResponse : ArrayList<FlightResponse.FlightResponseItem>(){
    data class FlightResponseItem(
        val arival: String,
        val arrivaldate: String,
        val createdAt: String,
        val departuredate: String,
        val destination: String,
        val details: List<Any>,
        val id: String,
        val name: String,
        val test: Test,
        val price:String
    ) {
        data class Test(
            val id: String,
            val data: String
        )
    }
}