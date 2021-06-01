package com.example.nasadailyimage

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.nasadailyimage.data.DailyImageDataClass
import com.example.nasadailyimage.network.RetroServiceInterface
import com.example.nasadailyimage.network.RetrofitNetworkClass
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class DailyImageViewModel(internal var application: Application) : AndroidViewModel(application) {


    var dailyImageDataClass: DailyImageDataClass? = null
    var sharedPreferences = application.getSharedPreferences("NasaResponse", Context.MODE_PRIVATE)
    var result = ""


    fun makeApiCall(updateContentListener: UpdateContentListener) {
        val todayDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val requiredDate: String = formatter.format(todayDate)
        result = sharedPreferences.getString("result", "").toString()
        val responseDate = sharedPreferences.getString("responseDate", "")
        if (result.isNotEmpty() && responseDate.equals(requiredDate)) {
            dailyImageDataClass =
                GsonBuilder().create().fromJson(result, DailyImageDataClass::class.java)
            sendContent(dailyImageDataClass, updateContentListener, false)
        } else {
            makeApiCalltoTheServer(updateContentListener, requiredDate)

        }
    }

    fun makeApiCalltoTheServer(updateContentListener: UpdateContentListener, requiredDate: String) {
        var api_key = "5Ga7g2QjGBjAw3IvdMnKfdMbgxqAiCZw7ZYymmMr"
        val retrofitNetworkClass = RetrofitNetworkClass()
        val retrofit = retrofitNetworkClass.getRetrofitInstance()
        val retroInstance = retrofitNetworkClass.getRetroServiceInterfaceInstance(retrofit)
        var call: Call<DailyImageDataClass> =
            retroInstance.getResponseFromApi(api_key, requiredDate)
        call.enqueue(object : Callback<DailyImageDataClass> {
            override fun onResponse(
                call: Call<DailyImageDataClass>,
                response: Response<DailyImageDataClass>
            ) {
                if (response.isSuccessful) {
                    dailyImageDataClass = response.body()
                    val jsonString = GsonBuilder().create().toJson(dailyImageDataClass)
                    var editor = sharedPreferences.edit()
                    editor.putString("result", jsonString)
                    editor.commit()
                    updateContentListener.hideErrorMsg()
                    sendContent(dailyImageDataClass, updateContentListener, true)
                } else {
                    updateContentListener.showErrorMsg(
                        "response is not successful" +
                                "the last message we have"
                    )
                    dailyImageDataClass =
                        GsonBuilder().create().fromJson(result, DailyImageDataClass::class.java)
                    sendContent(dailyImageDataClass, updateContentListener, false)
                }
            }

            override fun onFailure(call: Call<DailyImageDataClass>, t: Throwable) {
                updateContentListener.showErrorMsg(
                    "Not connected to the internet,showing you" + " " +
                            "the last message we have"
                )
                dailyImageDataClass =
                    GsonBuilder().create().fromJson(result, DailyImageDataClass::class.java)
                sendContent(dailyImageDataClass, updateContentListener, false)

            }

        })

    }

    fun sendContent(
        data: DailyImageDataClass?,
        updateContentListener: UpdateContentListener,
        isFromServer: Boolean
    ) {
        data?.let {
            updateContentListener.updateNasaImageTitle(it.title)
            updateContentListener.updateNasaImage(it.url)
            updateContentListener.updateNasaImageDesc(it.explanation)
            if (isFromServer) {
                val editor = sharedPreferences.edit()
                editor.putString("responseDate", it.date)
                editor.commit()
            }

        }
    }


    interface UpdateContentListener {
        fun updateNasaImageTitle(title: String)
        fun updateNasaImage(imageUrl: String)
        fun updateNasaImageDesc(desc: String)
        fun showErrorMsg(content: String)
        fun hideErrorMsg()
    }


}