package com.example.sunnyweather.android.logic.Repository

import androidx.lifecycle.liveData
import com.example.sunnyweather.android.logic.model.Place
import com.example.sunnyweather.android.logic.model.Weather
import com.example.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object NetworkRepository{
    fun searchPlaces(query:String)= liveData(Dispatchers.IO){
        val result=try {
            val placeResponse=SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }
            else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO){
       coroutineScope {
           val deferredRealtime=async {
               SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
           }
           val deferredDaily=async {
               SunnyWeatherNetwork.getDailyWeather(lng,lat)
           }
           val dailyResponse=deferredDaily.await()
           val realtimeResponse=deferredRealtime.await()
           if(dailyResponse.status=="ok"&&realtimeResponse.status=="ok"){
               val weather=Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
           }
           else{
               Result.failure(
                   RuntimeException(
                       "realtime response status is ${realtimeResponse.status}"+
                       "daily response status is ${dailyResponse.status}"
                   )
               )
           }
       }
    }/*
    coroutineScope
    会等待其所有的子协程执行完毕后在停止，那么就是为了保证里面的两个异步协程执行完毕
    他会创建新的协程，然后挂起协程协程内的子协程执行完毕
    */
    private fun <T> fire(context: CoroutineContext,block:suspend ()->Result<T>)=liveData<Result<T>>(context){
        val result=try{
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }//liveData会提供一个协程，Dispatcher.io 是一个调度器，调度器会选择一个合适的线程或者线程池来执行协程
}