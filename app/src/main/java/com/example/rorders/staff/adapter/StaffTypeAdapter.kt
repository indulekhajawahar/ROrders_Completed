package com.example.rorders.staff.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.adapter.CategoryAdapter
import com.example.rorders.admin.adapter.ItemsAdapter
import com.example.rorders.admin.model.ItemStatusModel
import com.example.rorders.admin.model.MenuDetailModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffTypeAdapter (
    private var mContext: Context,
    private var categoryArrayList: MutableList<String?>,
    var newOrdrBtn: Button
) :

    RecyclerView.Adapter<StaffTypeAdapter.MyViewHolder>() {
    //var isArrowClicked:Boolean=false
    lateinit var itemList:MutableList<String?>
    lateinit var itemTypeList:ArrayList<String>
    lateinit var menuItemNameList:MutableList<String?>
    lateinit var itemNameList:ArrayList<ItemStatusModel>
    lateinit var firebaseDatabase: DatabaseReference
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryNameTxt: TextView = view.findViewById(R.id.cat_name)
        var linearMain: LinearLayout =view.findViewById(R.id.linear_main)
        var itemsRecycler: RecyclerView =view.findViewById(R.id.items_rec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        itemList= ArrayList()
        itemTypeList= ArrayList()
        menuItemNameList= ArrayList()
        itemNameList= ArrayList()
        holder.itemsRecycler.visibility= View.VISIBLE


        holder.categoryNameTxt.text = categoryArrayList[position]

        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*val value: String? = dataSnapshot.getValue(String::class.java)
                Log.d("TAG", value.toString())*/
                Log.e("datchange","true")
                menuItemNameList = ArrayList()
                for (ds in dataSnapshot.children) {
                    val menuItem = ds.key
                    var type=ds.value
                    itemList.add(menuItem.toString())
                    if(categoryArrayList[position]==type){
                        menuItemNameList.add(menuItem.toString())

                    }else{
                        Log.e("item","con")
                    }


                    Log.e("TAG", menuItem.toString())
                    /* Log.e("listchange",menuItemNameList.size.toString())
                     menuRecycler.layoutManager=LinearLayoutManager(nContext)
                     var menuAdapter= StaffMenuAdapter(nContext,menuItemNameList)
                     menuRecycler.adapter=menuAdapter*/
                }
                Log.e("listchange",menuItemNameList.size.toString())
                holder.itemsRecycler.layoutManager= LinearLayoutManager(mContext)
                val menuAdapter= StaffMenuAdapter(mContext,menuItemNameList,newOrdrBtn,itemList)
                holder.itemsRecycler.adapter=menuAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
       /* Log.e("menuItemNameList",menuItemNameList.size.toString())
        holder.itemsRecycler.layoutManager= LinearLayoutManager(mContext)
        var adapter= ItemsAdapter(mContext,categoryArrayList[position].detailList,
            itemList,itemNameList,itemTypeList)
        holder.itemsRecycler.adapter=adapter*/


    }


    override fun getItemCount(): Int {
        return categoryArrayList.size
    }


}