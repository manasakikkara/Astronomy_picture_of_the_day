package com.example.nasadailyimage.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class RetrofitNetworkClass {



    var requiredUrl = "https://api.nasa.gov/"

    fun  getRetroServiceInterfaceInstance(retrofit:Retrofit):RetroServiceInterface{
        return retrofit.create(RetroServiceInterface::class.java)
    }

    fun getRetrofitInstance(): Retrofit {
      val retrofit = Retrofit.Builder()
            .baseUrl(requiredUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}