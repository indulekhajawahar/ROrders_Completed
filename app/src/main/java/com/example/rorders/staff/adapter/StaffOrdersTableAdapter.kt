package com.example.rorders.staff.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.kitchen.model.TableListModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffOrdersTableAdapter(
    private var nContext: Context,
    private var tableList:ArrayList<TableListModel>

) :

    RecyclerView.Adapter<StaffOrdersTableAdapter.MyViewHolder>() {
    var number:String=""
    lateinit var detailList:MutableList<String>
    lateinit var detailListCount:MutableList<String>

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tableNum: TextView = view.findViewById(R.id.table_num)
        var detailOrderRec: RecyclerView =view.findViewById(R.id.table_detail_rec)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_staff__ordertable_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var tables: TableListModel = tableList.get(position)
        number=tableList[position].tableNum.toString()
        holder.tableNum.text="Table " + tableList[position].tableNum


        orderDetail(holder.detailOrderRec)


    }

    override fun getItemCount(): Int {
        return tableList.size
    }
    private fun orderDetail(detailOrderRec: RecyclerView){
        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number).child("menu")
        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*val value: String? = dataSnapshot.getValue(String::class.java)
                Log.d("TAG", value.toString())*/

                detailList = ArrayList()
                detailListCount=ArrayList()
                for (ds in dataSnapshot.children) {
                    val menuItem = ds.key
                    var value=ds.value
                    if (menuItem.toString().contains("status")){
                        Log.e("st","con")
                    }else{
                        detailList.add(menuItem.toString())
                        detailListCount.add(value.toString())
                        Log.e("num", menuItem.toString())
                    }

                }

                detailOrderRec.layoutManager= LinearLayoutManager(nContext)
                val menuAdapter= StaffTableDetailAdapter(nContext,detailList,detailListCount)
                detailOrderRec.adapter=menuAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
    }
}