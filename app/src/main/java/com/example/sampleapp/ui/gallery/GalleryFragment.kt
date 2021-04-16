package com.example.sampleapp.ui.gallery

import android.Manifest
import android.R.attr.*
import android.content.ContentResolver
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.sampleapp.*
import kotlinx.android.synthetic.main.contact_card.view.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.*


class GalleryFragment : Fragment(), CompoundButton.OnCheckedChangeListener {
    var contactadapter: ContactAdapter?=null
    val db by  lazy {
        Room.databaseBuilder(requireContext(), appdatabase::class.java, "contact.db")
            .fallbackToDestructiveMigration().build()
    }
      lateinit var builder:MutableList<contact>
  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {


    return inflater.inflate(R.layout.fragment_gallery, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   builder=ArrayList()
      runBlocking {
          var lis: Deferred<MutableList<contact>?> = GlobalScope.async(Dispatchers.IO) {
              db.contactdata().getAlluser()
          }
          for (item in lis.await()!!) {
              builder.add(item)
          }
      }
      if(builder.size>0) {
          contactadapter = ContactAdapter(builder, this, db)
          rvcontact1.layoutManager = LinearLayoutManager(requireContext())
          rvcontact1.adapter = contactadapter


          }
      floatingActionButton.setOnClickListener {
          var intent = Intent(context, ScrollingActivity::class.java)
          startActivity(intent)
      }
      }




    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Toast.makeText(requireContext(), "User Selected", Toast.LENGTH_LONG).show()
    }
}



