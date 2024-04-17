package com.example.sunnyweather.android.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunnyweather.android.logic.Repository.NetworkRepository
import com.example.sunnyweather.android.logic.Repository.PlaceRepository
import com.example.sunnyweather.android.logic.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel :ViewModel(){
    private val locationLiveData=MutableLiveData<Location>()
    var locationLng=""
    var locationLat=""
    var placeName=""

    val weatherLiveData=Transformations.switchMap(locationLiveData){location->
        NetworkRepository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value=Location(lng,lat)
    }
}