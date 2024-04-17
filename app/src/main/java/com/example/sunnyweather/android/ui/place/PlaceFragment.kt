package com.example.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.android.ui.weather.WeatherActivity

class PlaceFragment:Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this)[PlaceViewModel::class.java]
    }
    private lateinit var adapter: PlaceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchPlaceEdit:EditText
    private lateinit var bgImage:ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView=view.findViewById(R.id.recyclerView)
        searchPlaceEdit=view.findViewById(R.id.searchPlaceEdit)
        bgImage=view.findViewById(R.id.bgImageView)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.findAllPlace().observe(viewLifecycleOwner){places->
            Log.d("PlaceFragment","被调用！！！")
            if(!places.isNullOrEmpty()){
                val intent=Intent(context,WeatherActivity::class.java)
                intent.putExtra("location_lng",places[0].lng)
                intent.putExtra("location_lat",places[0].lat)
                intent.putExtra("place_name",places[0].name)
                startActivity(intent)
                activity?.finish()
            }
        }
        val layoutManager=LinearLayoutManager(activity)
        adapter= PlaceAdapter(this,viewModel.placeList)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter
        searchPlaceEdit.addTextChangedListener {editable->
            val content=editable.toString()
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }
            else{
                recyclerView.visibility=View.GONE
                bgImage.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {result->
            val places=result.getOrNull()
            if(places!=null){
                recyclerView.visibility=View.VISIBLE
                bgImage.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}