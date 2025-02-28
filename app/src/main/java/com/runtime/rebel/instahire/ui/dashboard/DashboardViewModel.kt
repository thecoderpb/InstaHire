package com.runtime.rebel.instahire.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runtime.rebel.instahire.model.JobItem
import com.runtime.rebel.instahire.repository.home.HomeRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _jobListings = MutableLiveData<List<JobItem>>()
    val jobListings: LiveData<List<JobItem>> = _jobListings

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun fetchJobListings() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val jobResponse = homeRepository.getJobListings()
                _jobListings.value = jobResponse.results
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load job listings: ${e.message}"
            }
        }
    }

}