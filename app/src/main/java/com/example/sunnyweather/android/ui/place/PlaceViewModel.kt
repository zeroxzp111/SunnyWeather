package com.example.sunnyweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.android.logic.model.Place
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.sunnyweather.android.logic.Repository.NetworkRepository
import com.example.sunnyweather.android.logic.Repository.PlaceRepository
import com.example.sunnyweather.android.logic.model.DataPlace
import com.example.sunnyweather.android.logic.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData=Transformations.switchMap(searchLiveData){ query->
        NetworkRepository.searchPlaces(query)
    }
    fun searchPlaces(query:String){
        searchLiveData.value=query
    }

    fun addPlace(place: DataPlace){
        viewModelScope.launch(Dispatchers.IO) {
            PlaceRepository.addPlace(place)
        }
    }

    fun clearAll(){
        viewModelScope.launch(Dispatchers.IO){
            PlaceRepository.clearAll()
        }
    }

    fun findAllPlace(): LiveData<List<DataPlace>> {
        return PlaceRepository.getAllPlace()
    }
}