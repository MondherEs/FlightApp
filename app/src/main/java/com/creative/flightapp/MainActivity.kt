package com.creative.flightapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.creative.flightapp.databinding.ActivityMainBinding
import com.creative.flightapp.viewModel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.R
import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.creative.flightapp.model.FlightResponse
import com.creative.flightapp.utils.ConnectivityStatus
import com.creative.flightapp.view.adapter.FlightAdapter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FlightViewModel by viewModels()
    private lateinit var countryName: List<String>
    private val calendar: Calendar = Calendar.getInstance()
    private var minDepartureDate: Long? = null
    private var flightList: ArrayList<FlightResponse.FlightResponseItem> = ArrayList()
    private var adapter: FlightAdapter? = null
    private var isFilted:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val connectivityLiveData = ConnectivityStatus(this)
        connectivityLiveData.observe(this) { isAvailable ->
            when (isAvailable) {
                true -> initData()
                else -> Toast.makeText(this, " Internet is Unavailable", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initData() {

        adapter = FlightAdapter(this, flightList)
        binding.recyclerviewSearch.adapter = adapter
        binding.recyclerviewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.arrivalTime.setOnClickListener {
            minDepartureDate = null
            showDatePicker(binding.arrivalTime)
        }
        binding.departureTime.setOnClickListener {
            showDatePicker(binding.departureTime)
        }
        binding.confirmSearch.setOnClickListener {
            confirmSearch(isFilted)
        }

        binding.filterbtn.setOnClickListener{
            if (binding.filterLayout.visibility == View.VISIBLE) {
                binding.filterLayout.visibility = View.GONE
                isFilted = false
            } else {
                binding.filterLayout.visibility = View.VISIBLE
                isFilted = true
            }
        }

        getCountryList()

    }

    private fun getCountryList() {
        viewModel.getCountry().observe(this) {
            countryName = extractNamesFromString(it.toString())
            val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, countryName)
            // Set the ArrayAdapter to the AutoCompleteTextViews
            binding.departureArrival.setAdapter(adapter)
            binding.departureDestination.setAdapter(adapter)

        }
    }

    private fun confirmSearch(isFilted: Boolean) {
        val departureArrivalText = binding.departureArrival.text.toString().trim()
        val departureDestinationText = binding.departureDestination.text.toString().trim()
        val departureTimeText = binding.departureTime.text.toString().trim()
        val arrivalTimeText = binding.arrivalTime.text.toString().trim()

        if (isFilted) {
            if (arrivalTimeText.isEmpty()) {
                // Handle the case where either departureTime or arrivalTime is empty
                // Show an error message or perform the necessary action
                Toast.makeText(this, "Arrival Date is empty", Toast.LENGTH_SHORT).show()
                return
            }
            if (departureTimeText.isEmpty()) {

                Toast.makeText(this, "Departure Date is empty", Toast.LENGTH_SHORT).show()
                return
            }

            // Convert departureTime and arrivalTime to Date objects for comparison
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val departureDate = dateFormat.parse(departureTimeText)
            val arrivalDate = dateFormat.parse(arrivalTimeText)

            // Check if arrivalDate is higher (later) than departureDate
            if (arrivalDate == null || arrivalDate.before(departureDate)) {
                // Handle the case where arrivalTime is not higher than departureTime
                // Show an error message or perform the necessary action
                Toast.makeText(this, "Departure Date Should be before Arrival Date", Toast.LENGTH_SHORT)
                    .show()

                return
            }
        }
        if (departureDestinationText.isEmpty()) {
            Toast.makeText(this, "Destination is empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (departureArrivalText.isEmpty()) {
            Toast.makeText(this, "Arrival is empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (departureArrivalText == departureDestinationText) {
            Toast.makeText(this,
                "Destination and Arrival should not be the same", Toast.LENGTH_SHORT).show()
            return
        }

        getFlightList()
    }

    private fun getFlightList() {
        binding.noflight.visibility = View.GONE
        binding.recyclerviewSearch.visibility = View.VISIBLE

        viewModel.getFlight().observe(this) {
           // flightList= it
            if (it.isNotEmpty()) {
                binding.noflight.visibility = View.GONE
                binding.recyclerviewSearch.visibility = View.VISIBLE
                val filteredFlights = it.filter { flight ->
                    (flight.destination == binding.departureArrival.text.toString()) &&
                    (flight.arival == binding.departureDestination.text.toString())

                }
                flightList.clear()
                flightList.addAll(filteredFlights)

                if (isFilted) {
                    val filteredFlightsv2 = flightList.filter { flight ->
                        (flight.departuredate == binding.departureTime.text.toString()) &&
                                (flight.arrivaldate == binding.arrivalTime.text.toString())

                    }
                    flightList.clear()
                    flightList.addAll(filteredFlightsv2)
                     }

                if (flightList.size>0)  adapter!!.getFlight(flightList)
                else {
                    binding.noflight.visibility = View.VISIBLE
                    binding.recyclerviewSearch.visibility = View.GONE
                }

                }
        }
    }

    private fun showDatePicker(textView: TextView) {
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateTextView(textView, calendar)
        }

        val datePickerDialog = DatePickerDialog(this,
            datePickerListener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set the minimum date to today if minDate is null or minDate is earlier than today
        val today = Calendar.getInstance()

        datePickerDialog.datePicker.minDate = today.timeInMillis

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    private fun updateTextView(textView: TextView, calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)
        textView.text = selectedDate
    }

    private fun extractNamesFromString(responseString: String): List<String> {
        val regex = Regex("name=([^,\\)]+)")
        return regex.findAll(responseString)
            .map { it.groupValues[1] }
            .toList()
            .filter { !it.startsWith("CountryResponseItem(") }
    }
}