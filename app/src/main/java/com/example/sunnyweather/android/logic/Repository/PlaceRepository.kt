package com.example.sunnyweather.android.logic.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.sunnyweather.android.SunnyWeatherApplication
import com.example.sunnyweather.android.logic.dao.PlaceDao
import com.example.sunnyweather.android.logic.database.AppDatabase
import com.example.sunnyweather.android.logic.model.DataPlace
import com.example.sunnyweather.android.logic.model.Location
import com.example.sunnyweather.android.logic.model.Place
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object PlaceRepository {
    private val dao=AppDatabase.getDatabase(SunnyWeatherApplication.context)

    fun getAllPlace():LiveData<List<DataPlace>>{
        return dao.placeDao().getAllPlace()

    }

    suspend fun deletePlace(place: DataPlace){
        dao.placeDao().deletePlace(place)
    }

    suspend fun addPlace(place: DataPlace){
        dao.placeDao().insertPlace(place)
    }

    suspend fun clearAll(){
        dao.placeDao().deleteAllPlace()
    }

    suspend fun getCount(){
        dao.placeDao().getCount()
    }
}