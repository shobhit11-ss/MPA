package com.example.sampleapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.sampleapp.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.rvcontact1
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class HomeFragment : Fragment() {
    var contactadapter: notificationadapter? = null
    val db by lazy {
        Room.databaseBuilder(requireContext(), appdatabase::class.java, "miscallcontact.db")
            .fallbackToDestructiveMigration().build()
    }
    lateinit var builder: MutableList<misscallcontact>

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        builder = ArrayList()
        runBlocking {
            var lis: Deferred<MutableList<misscallcontact>?> = GlobalScope.async(Dispatchers.IO) {
                db.contactdata().getAllmiss()
            }
            for (item in lis.await()!!) {
                builder.add(item)
            }
               Log.e("tag","${builder.size}")
        }
        if (builder.size > 0) {
            contactadapter = notificationadapter(builder, db)
            rvcontact2.layoutManager = LinearLayoutManager(requireContext())
            rvcontact2.adapter = contactadapter


        }
    }
}
