package com.example.rorders.kitchen.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.adapter.ItemsAdapter
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.ItemStatusModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class KitchenMenuItemsAdapter (
    private var mContext: Context,
    private var itemsArrayList: ArrayList<ItemListModel>,
    private var itemList:ArrayList<String>,
    var itemNameList:ArrayList<ItemStatusModel>
) :

    RecyclerView.Adapter<KitchenMenuItemsAdapter.MyViewHolder>() {
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


        Log.e("itemstotal",itemNameList[position].status)
        holder.itemName.text = itemNameList[position].itemName
        if (itemNameList[position].status=="0"){
            holder.switch.isClickable=false
            Log.e("st0",itemNameList[position].itemName)
            holder.switch.isChecked=false
        }else{
            holder.switch.isClickable=true
            Log.e("st1",itemNameList[position].itemName)
            holder.switch.isChecked=true
        }
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
        holder.switch.setOnCheckedChangeListener( { buttonView, isChecked ->

            if (isChecked){
                itemNameList[position].status="0"
                firebaseDatabase.child(itemList[position]).setValue(true);

            }else{
                itemNameList[position].status="1"
                firebaseDatabase.child(itemList[position]).removeValue()

            }

        })

    }


    override fun getItemCount(): Int {
        return itemList.size
    }


}