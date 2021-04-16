package com.example.sampleapp
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton

import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.example.sampleapp.appdatabase
import com.example.sampleapp.contact
import com.example.sampleapp.misscallcontact
import kotlinx.android.synthetic.main.contact_card.view.*
import kotlinx.android.synthetic.main.notificaiton_card.view.*
import kotlinx.coroutines.*

class notificationadapter(var builder:MutableList<misscallcontact>, val db: appdatabase): RecyclerView.Adapter<notificationadapter.contactviewholder>(){
   inner class contactviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            user: misscallcontact,
            db: appdatabase,
            index: Int
        ) {


            itemView.imageButton.setOnClickListener {
                runBlocking {
                    GlobalScope.async(Dispatchers.IO) {
                        db.contactdata().deletecontactm(user.phone_number)

                    }

                    deleteItem(index)
                }

            }
        }
    }



    val builder1=builder.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):contactviewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notificaiton_card, parent, false)
        return contactviewholder(itemView)
    }



    override fun onBindViewHolder(holder: contactviewholder, position: Int) {
        holder.itemView.contactname1.text=builder!![position].name
        holder.itemView.phone1.text=builder!![position].phone_number

        holder.bind(builder!![position],db,position)

    }

    override fun getItemCount(): Int =builder!!.size

     fun deleteItem(index: Int){
        builder.removeAt(index)
        notifyDataSetChanged()
    }

    }





