package com.example.sunnyweather.android.logic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status:String,val places:List<Place>)

data class Place(val name:String,val location:Location,@SerializedName("formatted_address")val address:String)

@Entity(tableName = "Place")
data class Location(@ColumnInfo(name="lng")val lng:String,@ColumnInfo(name="lat")val lat:String){
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
}