package com.example.sunnyweather.android.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sunnyweather.android.logic.model.DataPlace
import com.example.sunnyweather.android.logic.model.Location
@Dao
interface PlaceDao {
    @Insert
    suspend fun insertPlace(place: DataPlace):Long

    @Delete
    suspend fun deletePlace(place:DataPlace)

    @Query("select * from Place ")
    fun getAllPlace():LiveData<List<DataPlace>>

    @Query("delete from Place")
    suspend fun deleteAllPlace()

    @Query("select count(*) from Place")
    suspend fun getCount():Long
}