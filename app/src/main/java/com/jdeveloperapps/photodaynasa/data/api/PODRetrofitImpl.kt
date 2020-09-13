package com.jdeveloperapps.photodaynasa.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PODRetrofitImpl {

    fun getRetrofitImpl(): PictureOfTheDayAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
        return podRetrofit.create(PictureOfTheDayAPI::class.java)
    }

}