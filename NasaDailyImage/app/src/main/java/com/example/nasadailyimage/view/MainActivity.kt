package com.example.nasadailyimage.view

//import org.json.JSONException
//import org.json.JSONObject

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nasadailyimage.DailyImageViewModel
import com.example.nasadailyimage.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: DailyImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var androidViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel = androidViewModelFactory.create(DailyImageViewModel::class.java)
        handleApiCall()
    }

    private fun handleApiCall() {
        viewModel.makeApiCall(object : DailyImageViewModel.UpdateContentListener {
            override fun updateNasaImageTitle(title: String) {
                nasa_image_title.text = title

            }

            override fun updateNasaImage(imageUrl: String) {
                Glide.with(this@MainActivity).load(imageUrl).into(nasa_image)
            }

            override fun updateNasaImageDesc(imageDesc: String) {
                nasa_about_image.text = "Explanation:"
                nasa_image_desc.text = imageDesc
            }

            override fun showErrorMsg(content: String) {
                error_message.visibility = View.VISIBLE
                error_message.text = content
            }

            override fun hideErrorMsg() {
                error_message.visibility = View.GONE
            }

        })
    }


}