package com.example.rorders.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.model.ItemListModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.database.*
import com.google.firebase.database.ktx.snapshots


class ItemsAdapter(
    private var mContext: Context,
    private var itemsArrayList: ArrayList<ItemListModel>,
    private var itemList:ArrayList<String>,
    var menuUpdateList:MutableList<String?>
) :

    RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {
    var isArrowClicked:Boolean=false
    lateinit var itemNameList:ArrayList<String>
    lateinit var menuItemNameList:ArrayList<String>
    lateinit var database: DatabaseReference
    var menuItemUpdatedList: MutableList<String?> = ArrayList()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.itemname)
        var switch:SwitchMaterial=view.findViewById(R.id.select_switch)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.e("menuitemlist item",menuUpdateList.size.toString())
        Log.e(" items total",itemList.size.toString())
        holder.itemName.text = itemList[position]
       /* for(i in itemList.indices){
            for (j in menuUpdateList.indices){
                if (itemList[i]==menuUpdateList[j].toString()){
                    Log.e("i eq",itemList[i])
                    Log.e("j eq",menuUpdateList[j].toString())
                    holder.switch.isChecked=true
                }
                else{
                    Log.e("i",itemList[i])
                    Log.e("j",menuUpdateList[j].toString())
                    holder.switch.isChecked=false
                }
            }
        }*/


        itemNameList= ArrayList()
        menuItemNameList= ArrayList()

        database = FirebaseDatabase.getInstance().getReference("TodaysMenu")


        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        /*  menuItemNameList.addAll( AppController.menuArrayList)
          Log.e("menusize",AppController.menuArrayList.size.toString())*/
        /*for (i in itemList.indices){
            for (j in menuItemNameList.indices){
                Log.e("iiiii",itemList[i])
                Log.e("jjjjj",menuItemNameList[j])

                if (itemList[i]==menuItemNameList[j]){
                    Log.e("true","strue")
                    holder.switch.isChecked=true
                    break
                }
                else{
                    Log.e("false","sfalse")
                    holder.switch.isChecked=false
                }
            }
        }*/

        holder.switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                database.child(itemList[position]).setValue(true);

            }else{
                database.child(itemList[position]).removeValue()

            }

        })

    }


    override fun getItemCount(): Int {
        return itemList.size
    }


}