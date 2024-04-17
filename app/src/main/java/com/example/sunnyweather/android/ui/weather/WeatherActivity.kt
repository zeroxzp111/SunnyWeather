package com.example.sunnyweather.android.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.android.logic.model.Location
import com.example.sunnyweather.android.logic.model.Weather
import com.example.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy{ ViewModelProvider(this)[WeatherViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView=window.decorView
        decorView.systemUiVisibility=
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor= Color.TRANSPARENT
        setContentView(R.layout.activity_weather)
        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLng=intent.getStringExtra("location_lng") ?: ""
        }
        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra("location_lat") ?: ""
        }
        if(viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer {result->
            val weather=result.getOrNull()
            if(weather!=null){
                showWeatherInfo(weather)
            }
            else{
                Toast.makeText(this,"无法成功获取天气",Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }
    private fun showWeatherInfo(weather: Weather){
        val placeName=findViewById<TextView>(R.id.n_placeName)
        placeName.text=viewModel.placeName
        val realTime=weather.realtime
        val daily=weather.daily
        val currentTemp=findViewById<TextView>(R.id.currentTemp)
        val currentTempText="${realTime.temperature.toInt()} ℃"
        currentTemp.text=currentTempText
        val currentPM25Text="空气指数${realTime.airQuality.aqi.chn.toInt()}"
        val currentAQT=findViewById<TextView>(R.id.currentAQI)
        currentAQT.text=currentPM25Text
        val nowLayout=findViewById<RelativeLayout>(R.id.nowLayout)
        nowLayout.setBackgroundResource(getSky(realTime.skycon).bg)
        val forecastLayout=findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for(i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view=LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dateInfo=view.findViewById(R.id.dateInfo) as TextView
            val skyIcon=view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo=view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo=view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat=SimpleDateFormat("yyy-MM-dd", Locale.getDefault())
            dateInfo.text=simpleDateFormat.format(skycon.date)
            val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            val tempText="${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text=tempText
            forecastLayout.addView(view)
        }
        val lifeIndex=daily.lifeIndex
        val coldRiskText=findViewById<TextView>(R.id.coldRiskText)
        coldRiskText.text=lifeIndex.coldRisk[0].desc
        val dressingText=findViewById<TextView>(R.id.dressingText)
        dressingText.text=lifeIndex.dressing[0].desc
        val ultravioletText=findViewById<TextView>(R.id.ultravioletText)
        ultravioletText.text=lifeIndex.ultraviolet[0].desc
        val carWashingText=findViewById<TextView>(R.id.carWashingText)
        carWashingText.text=lifeIndex.carWashing[0].desc
        val weatherLayout=findViewById<ScrollView>(R.id.weatherLayout)
        weatherLayout.visibility=View.VISIBLE
    }
}