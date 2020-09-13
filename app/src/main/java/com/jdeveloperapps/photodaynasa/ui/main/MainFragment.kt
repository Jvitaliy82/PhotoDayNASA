package com.jdeveloperapps.photodaynasa.ui.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.jdeveloperapps.photodaynasa.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<PictureOfTheDayData> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //showDialog("Ошибка", "Сообщение с сервера пустое")
                } else {
                    message.text = serverResponseData.explanation
                    image_view.load(url)
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showViewLoading()
            }
            is PictureOfTheDayData.Error -> {
                //showDialog("Ошибка!", data.error.message)
            }
        }
    }

}
