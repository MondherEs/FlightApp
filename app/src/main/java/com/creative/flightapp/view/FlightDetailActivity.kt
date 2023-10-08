package com.creative.flightapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.creative.flightapp.MainActivity
import com.creative.flightapp.databinding.ActivityFlightDetailBinding
import com.creative.flightapp.viewModel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlightDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlightDetailBinding
    private val viewModel: FlightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        val position = getFlightID()
        if (position != -1)
        {
            getCountryList(position)
        }
    }

    private fun initData() {
        binding.back.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun getFlightID(): Int {
        return intent.getIntExtra("flightPos", -1)
    }
    private fun getCountryList(number: Int) {
        viewModel.getFlightDetail(number).observe(this) {

            binding.startDate.text = it.departuredate
            binding.endDate.text = it.arrivaldate
            binding.arrival.text =it.destination
            binding.destination.text = it.arival
            binding.price.text = it.price
            if (it.test != null) binding.detail.text = it.test.data
        }
    }
}