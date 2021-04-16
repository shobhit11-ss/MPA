package com.example.sampleapp

import android.R.id.message
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log


class SmsService:Service() {
    var flag = false
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        flag=true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("tag","Message Sent")

            val phoneNumber = intent!!.extras!!.getString("number")
            val smsManager: SmsManager = SmsManager.getDefault()

            val parts = smsManager.divideMessage("Busy Right Now")
            smsManager.sendMultipartTextMessage(
                phoneNumber,
                null,
                parts,
                null,
                null
            )

            flag = false
            stopSelf()

        return super.onStartCommand(intent, flags, startId);
    }
}