package com.example.rorders.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.ItemStatusModel
import com.example.rorders.admin.model.ItemStatusModelNew
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FilteredItemAdapter (
    private var mContext: Context,
    private var filteredList:ArrayList<ItemStatusModelNew>
) :

    RecyclerView.Adapter<FilteredItemAdapter.MyViewHolder>() {
    var isArrowClicked:Boolean=false

    lateinit var menuItemNameList:ArrayList<String>
    lateinit var firebaseDatabase: DatabaseReference
    var menuItemUpdatedList: MutableList<String?> = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.itemname)
        var switch: Switch =view.findViewById(R.id.select_switch)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.switch.isClickable=true

        holder.itemName.text = filteredList[position].itemName
        if (filteredList[position].status=="0"){
            Log.e("st0",filteredList[position].itemName)
            holder.switch.isChecked=false
        }else{
            Log.e("st1",filteredList[position].itemName)
            holder.switch.isChecked=true
        }


        firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
        holder.switch.setOnCheckedChangeListener( { buttonView, isChecked ->

            if (isChecked){
                filteredList[position].status="0"
                firebaseDatabase.child(filteredList[position].itemName).setValue(filteredList[position].itemType);

            }else{
                filteredList[position].status="1"
                firebaseDatabase.child(filteredList[position].itemName).removeValue()

            }

        })

    }


    override fun getItemCount(): Int {
        return filteredList.size
    }


}