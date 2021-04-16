package com.example.sampleapp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sampleapp.R
import com.example.sampleapp.SharedPreference
import kotlinx.android.synthetic.main.fragment_slideshow.*

class SlideshowFragment : Fragment() {

  private lateinit var slideshowViewModel: SlideshowViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return inflater.inflate(R.layout.fragment_slideshow, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      submit.setOnClickListener {
          var sharedPreference: SharedPreference = SharedPreference(requireContext())
          sharedPreference.save("Message", edit1.text.toString())
          edit1.text!!.clear()
      }
      super.onViewCreated(view, savedInstanceState)
  }
}