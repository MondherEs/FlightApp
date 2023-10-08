package com.creative.flightapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.creative.flightapp.model.CountryResponse
import com.creative.flightapp.model.FlightDetailResponse
import com.creative.flightapp.model.FlightResponse
import com.creative.flightapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val mainRepo: MainRepository) : ViewModel() {

    private var job: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable -> throwable.printStackTrace() }
    private val loading = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    var countryList = MutableLiveData<CountryResponse>()
    var flightList = MutableLiveData<FlightResponse>()
    var flightDetailList = MutableLiveData<FlightDetailResponse>()

    fun getCountry():LiveData<CountryResponse> {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepo.getCountry()
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                if (response.isSuccessful) {
                    countryList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
        return countryList
    }

    fun getFlight():LiveData<FlightResponse> {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepo.getFlight()
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                if (response.isSuccessful) {
                    flightList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
        return flightList
    }
    fun getFlightDetail(number: Int):LiveData<FlightDetailResponse> {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepo.getFlightDetail(number)
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                if (response.isSuccessful) {
                    flightDetailList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
        return flightDetailList
    }
    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }


}