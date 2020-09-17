package com.jdeveloperapps.photodaynasa.ui.main

import android.app.DatePickerDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.jdeveloperapps.photodaynasa.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel
    private lateinit var hdUrl: String


    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<PictureOfTheDayData> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.sendServerRequest()

        image_view.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(hdUrl)
            findNavController().navigate(action)
        }

    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //showDialog("Ошибка", "Сообщение с сервера пустое")
                } else {
                    showViewLoading(false)
                    message.text = serverResponseData.explanation
                    image_view.load(url) {
                        placeholder(R.drawable.ic_photo)
                        error(R.drawable.ic_cancel)
                    }
                    hdUrl = serverResponseData.url
                }
            }
            is PictureOfTheDayData.Loading -> {
                showViewLoading(true)
            }
            is PictureOfTheDayData.Error -> {
                //showDialog("Ошибка!", data.error.message)
            }
        }
    }

    private fun showViewLoading(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.calendar -> {
                showCalendar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showCalendar() {
        DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                viewModel.setNewDate(year, month, dayOfMonth)
                viewModel.sendServerRequest()
            },
            viewModel.getCurrentDate().get(Calendar.YEAR),
            viewModel.getCurrentDate().get(Calendar.MONTH),
            viewModel.getCurrentDate().get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}