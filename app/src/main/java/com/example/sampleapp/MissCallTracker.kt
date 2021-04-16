package com.example.sampleapp

import android.content.*
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.room.Room
import com.firebase.ui.auth.AuthUI.getApplicationContext
import kotlinx.coroutines.*
import kotlin.coroutines.getPolymorphicElement


class MissCallTracker : BroadcastReceiver() {
    companion object {
        var ring: Boolean = false
        var callReceived: Boolean = false
        var StringPhoneNumber: String? = null
        var context1: Context? = null
        var resolver: ContentResolver? = null
        fun fetch(context: Context, limit: Int, offset: Int): ArrayList<MutableList<String>>? {
            resolver = context.getContentResolver();
            val call1: ArrayList<MutableList<String>>? = ArrayList()
            val cursor: Cursor = createCursor(limit, offset)
            cursor.moveToFirst()
            val idxNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val idxType = cursor.getColumnIndex(CallLog.Calls.TYPE)
            while (!cursor.isAfterLast()) {

                call1?.add(
                    mutableListOf<String>(
                        cursor.getString(idxNumber), cursor.getString(
                            idxType)
                    )
                )
                cursor.moveToNext();
            }
            cursor.close();
            return call1;
        }

        fun createCursor(limit: Int, offset: Int): Cursor {
            val sortOrder = CallLog.Calls.DATE + " DESC limit " + limit + " offset " + offset
            return resolver!!.query(
                CallLog.Calls.CONTENT_URI, arrayOf(
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                ),
                null,
                null,
                sortOrder
            )!!
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        context1 = context
        val db by lazy {
            Room.databaseBuilder(context, appdatabase::class.java, "contact.db")
                .fallbackToDestructiveMigration().build()
        }
        val db1 by lazy {
            Room.databaseBuilder(context, appdatabase::class.java, "miscallcontact.db")
                .fallbackToDestructiveMigration().build()
        }
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state == null) {
            return;
        }
        val bundle: Bundle? = intent.extras;
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            ring = true;
            // Get the Caller's Phone Number

        }
        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }



        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

            Log.e("tag", "phone Number ")
            if (ring == true && callReceived == false) {

                val callLogDetails: ArrayList<MutableList<String>>? = fetch(context, 1, 0)

                val sharedPreference: SharedPreference = SharedPreference(context)
                Log.e("TAG","${callLogDetails!![0][1]}")
                if ((callLogDetails!![0][1]=="3" || callLogDetails[0][1]=="5" || callLogDetails[0][1] == "10"
                            ) && sharedPreference.getValueBoolien("AwayMode", false)
                ) {

                    runBlocking {

                        Log.e(
                            "tag",
                            "Service Started ${callLogDetails[0][0].length}"
                        )

                        val lis: Deferred<contact?> = GlobalScope.async(Dispatchers.IO) {

                            db.contactdata().checkUserExist(callLogDetails[0][0])
                        }
Log.e("tag","${lis.await().toString()}")

                        if (lis.await() != null) {
                            val lis1: Deferred<misscallcontact?> =
                                GlobalScope.async(Dispatchers.IO) {
                                    db1.contactdata().checkAllmiss(callLogDetails[0][0])
                                }
                            Log.e("Tag", "Hi")
                            if (lis1.await() == null) {
                                db1.contactdata().insertm(
                                    misscallcontact(
                                        lis.await()!!.name,
                                        lis.await()!!.phone_number
                                    )
                                )
                            }

                            val smsManager: SmsManager = SmsManager.getDefault()
                            var s = sharedPreference.getValueString("Message")
                            Log.e("tag","Hi ${s}")
                            smsManager.sendTextMessage(
                                callLogDetails[0][0],
                                null, s,
                                null,
                                null
                            )
                            Log.e("tag", "Service Finished")
                        }
                    }
                    }
                }



            ring = false;
        }
        callReceived = false;
    }
}