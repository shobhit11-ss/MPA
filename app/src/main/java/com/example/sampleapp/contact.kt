package com.example.sampleapp

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
data class contact(val name:String,val phone_number:String,@PrimaryKey(autoGenerate = true)val id:Long?=null)
