package com.example.sampleapp

import android.Manifest
import android.content.ContentResolver
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.content_scrolling.*


class ScrollingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    var contactadapter: ContactAdapter? = null
    val db by lazy {
        Room.databaseBuilder(this, appdatabase::class.java, "contact.db")
            .fallbackToDestructiveMigration().build()
    }
    lateinit var builder: MutableList<contact>

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Select Contact"
        loadContacts()

        contactadapter = ContactAdapter(builder, this, db)
        rvcontact.layoutManager = LinearLayoutManager(this)
        rvcontact.adapter = contactadapter
        searchBar.addTextChangeListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.e("Checking", "${searchBar!!.text}")
                contactadapter!!.getFilter()!!.filter(searchBar!!.text);
            }

            override fun afterTextChanged(editable: Editable) {


            }
        })
    }

    private fun loadContacts() {
        builder =ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )

        } else {
            builder = getContacts()

        }
    }

    val PROJECTION = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    private fun getContacts(): MutableList<contact> {

        val mContactList: MutableList<contact> = ArrayList()
        val cr = contentResolver
        val cursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var name: String?
                var number: String?
                while (cursor.moveToNext()) {

                    name = cursor.getString(nameIndex)
                    number = cursor.getString(numberIndex).replace("\\s".toRegex(),"")
                    if(number[0]!='+')
                        number="+91"+number
                    Log.e("TAg","${name}==${number[0]}")
                    mContactList.add(contact(name, number))
                }
            } finally {
                cursor.close()
            }
        }
        return mContactList
    }
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Toast.makeText(this, "User Selected", Toast.LENGTH_LONG).show()
    }
}