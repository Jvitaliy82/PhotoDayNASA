package com.jdeveloperapps.photodaynasa.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainViewModel(
        private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
        private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        retrofitImpl.getRetrofitImpl().getPictureOfTheDay("DEMO_KEY").enqueue(object :
                Callback<PODServerResponseData> {

            override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(response.body()!!)
                } else {
                    liveDataForViewToObserve.value =
                            PictureOfTheDayData.Error(Throwable("Unidentified error"))
                }
            }

            override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
            }
        })
    }

}

data class PODServerResponseData(
        val date: String?,
        val explanation: String?,
        val hdurl: String?,
        val media_type: String?,
        val service_version: String?,
        val title: String?,
        val url: String?
)

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: PODServerResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>
}

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



