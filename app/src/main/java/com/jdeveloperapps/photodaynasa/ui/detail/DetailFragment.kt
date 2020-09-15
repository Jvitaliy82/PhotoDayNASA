package com.jdeveloperapps.photodaynasa.ui.detail

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.jdeveloperapps.photodaynasa.R
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: DetailFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        image_view_detail.load(args.url)
    }

}