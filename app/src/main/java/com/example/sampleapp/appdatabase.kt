package com.example.sampleapp

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [contact::class,misscallcontact::class],version=2)

abstract class appdatabase:RoomDatabase() {
    abstract fun contactdata():contactdata
}