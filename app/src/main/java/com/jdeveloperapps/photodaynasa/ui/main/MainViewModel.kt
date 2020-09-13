package com.jdeveloperapps.photodaynasa.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdeveloperapps.photodaynasa.data.api.PODRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        retrofitImpl.getRetrofitImpl().getPictureOfTheDay("2020-09-09","DEMO_KEY").enqueue(object :
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




