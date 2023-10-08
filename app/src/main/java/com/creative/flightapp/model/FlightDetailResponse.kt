package com.creative.flightapp.model

data class FlightDetailResponse(
    val arival: String,
    val arrivaldate: String,
    val createdAt: String,
    val departuredate: String,
    val destination: String,
    val details: List<Any>,
    val id: String,
    val name: String,
    val price: String,
    val test: Test?
) {
    data class Test(
        val id: String,
        val data: String
    )
}