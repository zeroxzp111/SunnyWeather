package com.example.sunnyweather.android.logic.network

import com.example.sunnyweather.android.SunnyWeatherApplication
import com.example.sunnyweather.android.logic.model.DailyResponse
import com.example.sunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng:String,@Path("lat")lat:String):Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.jons")
    fun getDailyWeather(@Path("lng")lng: String,@Path("lat")lat: String):Call<DailyResponse>
}