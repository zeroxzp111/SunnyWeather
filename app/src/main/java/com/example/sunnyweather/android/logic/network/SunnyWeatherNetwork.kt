package com.example.sunnyweather.android.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val placeService=ServiceCreator.create<PlaceService>()
    private val weatherService=ServiceCreator.create<WeatherService>()
    suspend fun searchPlaces(query:String)= placeService.searchPlaces(query).await()
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailyWeather(lng,lat).await()
    suspend fun getRealtimeWeather(lng: String,lat: String)= weatherService.getRealtimeWeather(lng,lat).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {continuation ->  
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null)continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
            /*
            suspendCoroutine会挂起调用它的线程然后执行lambda表达式里的任务，等待里面执行结束后再调用resumeWithException后继续进行协程
            suspendCoroutine 是一个挂起函数，它会暂停当前协程的执行，然后在 Lambda 表达式中执行指定的代码。Lambda 表达式中的代码可以是异步操作，
            例如回调式 API。当异步操作完成时，通过调用 continuation.resume 或 continuation.resumeWithException 来恢复挂起的协程，并返回结果或异常。

            coroutineScope 是一个协程构建器，它创建一个新的协程作用域，并在其中启动新的协程。在 coroutineScope 内部启动的协程将受该作用域的影响，并在作用域结束时取消。
            coroutineScope 函数本身是一个挂起函数，它会挂起当前协程，等待作用域内的所有协程执行完成后才会恢复执行
            */
        }
    }
}