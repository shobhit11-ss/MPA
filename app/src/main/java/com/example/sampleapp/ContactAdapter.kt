package com.example.sampleapp

import android.provider.Settings
import android.system.Os.bind
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery.bind
import kotlinx.android.synthetic.main.contact_card.view.*
import kotlinx.coroutines.*
import java.io.File
import java.security.AccessController.getContext


class ContactAdapter( var builder:MutableList<contact>,val oncheckchangelistener:CompoundButton.OnCheckedChangeListener,val db:appdatabase): RecyclerView.Adapter<ContactAdapter.contactviiewholder>() , Filterable {
    class contactviiewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            user: contact,
            clickListener: CompoundButton.OnCheckedChangeListener,
            db: appdatabase
        ) {

            runBlocking {
                var lis: Deferred<contact?> = GlobalScope.async(Dispatchers.IO) {
                    db.contactdata().checkUserExist(user.phone_number)
                }
                if (lis.await() != null) {
                    itemView.switch1.isChecked = true
                }


                itemView.switch1.setOnCheckedChangeListener { clickListener, isChecked ->
                    if (isChecked) {
                        GlobalScope.async(Dispatchers.IO) {
                            db.contactdata().insert(contact(user.name, user.phone_number))
                        }
                    } else {
                        GlobalScope.async(Dispatchers.IO) {
                            db.contactdata().deletecontact( user.phone_number)
                        }
                    }
                }
            }
        }
    }


val builder1=builder.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):contactviiewholder {
   val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
    return contactviiewholder(itemView)
    }



    override fun onBindViewHolder(holder: contactviiewholder, position: Int) {
        holder.itemView.contactname.text=builder!![position].name
        holder.itemView.phone.text=builder!![position].phone_number

                holder.bind(builder!![position],oncheckchangelistener,db)

    }

    override fun getItemCount(): Int =builder!!.size
 override   fun getFilter(): Filter? {
        return exampleFilter
    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList:  MutableList<contact> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(builder1!!)
            } else {
                     Log.e("Build","${builder1!!.size}")
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in builder1!!) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            builder!!.clear()
            builder!!.addAll(results.values as MutableList<contact>)
            notifyDataSetChanged()
        }
    }





}