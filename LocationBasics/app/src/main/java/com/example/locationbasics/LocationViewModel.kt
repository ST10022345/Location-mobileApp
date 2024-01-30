package com.example.locationbasics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle


import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val Location: State<LocationData?> = _location

    fun updateLocation(newLocation: LocationData){
_location.value = newLocation
    }
}