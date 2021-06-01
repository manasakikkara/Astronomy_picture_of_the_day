package com.example.nasadailyimage.network

import com.example.nasadailyimage.data.DailyImageDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

interface RetroServiceInterface {



    @GET("planetary/apod?")
    fun getResponseFromApi(@Query("api_key") apiKey: String, @Query("date") date: String)
            : Call<DailyImageDataClass>


}