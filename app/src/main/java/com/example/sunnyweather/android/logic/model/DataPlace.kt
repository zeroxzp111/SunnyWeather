package com.example.sunnyweather.android.logic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Place")
data class DataPlace(val lng:String,val lat:String,val name:String){
        @PrimaryKey(autoGenerate = true)
        var id:Long=0
}