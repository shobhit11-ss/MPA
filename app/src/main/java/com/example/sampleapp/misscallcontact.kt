package com.example.sampleapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class misscallcontact(val name:String,val phone_number:String,@PrimaryKey(autoGenerate = true)val id:Long?=null)