package com.example.sampleapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface contactdata{
    @Insert
    suspend fun insert(Contact:contact)
    @Delete
    suspend fun delete(Contact:contact)
    @Query("Delete from contact where phone_number=:phone")
    suspend fun deletecontact(phone: String)
    @Query( "Select name,phone_number from contact ")
   suspend fun getAlluser():MutableList<contact>
   @Query("Select name,phone_number from contact where phone_number=:phone")
   suspend fun checkUserExist(phone:String): contact
   @Insert
   suspend fun insertm(Contact:misscallcontact)
    @Query( "Select name,phone_number from misscallcontact ")
    suspend fun getAllmiss():MutableList<misscallcontact>
    @Query("Select name,phone_number from misscallcontact where phone_number=:phone")
    suspend fun checkAllmiss(phone: String):misscallcontact
    @Query("Delete from misscallcontact where phone_number=:phone")
    suspend fun deletecontactm(phone: String)
}