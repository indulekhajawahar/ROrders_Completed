package com.example.rorders.kitchen.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.kitchen.model.TableListModel
import com.example.rorders.staff.adapter.StaffTableAdapter
import com.example.rorders.staff.adapter.StaffTableDetailAdapter
import com.example.rorders.staff.model.OrderDetailModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KitchenTableAdapter (
    private var nContext: Context,
    private var tableList:ArrayList<TableListModel>

) :

    RecyclerView.Adapter<KitchenTableAdapter.MyViewHolder>() {
    var number:String=""
    lateinit var detailList:MutableList<String>
    lateinit var detailListCount:MutableList<String>
    lateinit var statusList:MutableList<String>
    lateinit var orderDetailList:ArrayList<OrderDetailModel>

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tableNum: TextView = view.findViewById(R.id.table_num)
        var detailOrderRec: RecyclerView =view.findViewById(R.id.table_detail_rec)
        var tableBtn: Switch =view.findViewById(R.id.table_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_kitchen_table_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
detailList=ArrayList()
        detailListCount=ArrayList()

        var tables: TableListModel = tableList.get(position)
        number=tableList[position].tableNum.toString()
        holder.tableNum.text="Table " + tableList[position].tableNum

        if (tables.tableStatus == "1")
        {
            holder.tableBtn.isChecked = true

        } else
        {
            holder.tableBtn.isChecked = false

        }
        holder.tableBtn.setOnCheckedChangeListener{ buttonView, isChecked ->

            /*database= FirebaseDatabase.getInstance().getReference("StudentLists").
            child(employee_array.get(position).name.toString()).child("status")*/
            var database: DatabaseReference
            if (isChecked)
            {
                database = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number)
                database.child("status").setValue("1")
                Log.e("Checked_Success",isChecked.toString())

                holder.tableBtn.isChecked = true

                /*database.setValue("0")
                        holder.switch_button.isChecked=true*/

            }
            else
            {
                database = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number)
                database.child("status").setValue("0")
                Log.e("checked_error",isChecked.toString())

                holder.tableBtn.isChecked = false
                /*database.setValue("1")
                            holder.switch_button.isChecked=false*/

            }


        }

        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number).child("menu")
        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                statusList = ArrayList()
                orderDetailList = ArrayList()

                for (ds in dataSnapshot.children) {
                    val menuItem = ds.key
                    var value=ds.value
                    var statusValue=ds.child("status").value.toString()
                    if (menuItem.toString()!="status"){
                        val temp = OrderDetailModel(menuItem.toString(), value.toString(), statusValue)

                        orderDetailList.add(temp)
                    }

                }
Log.e("detailsize",orderDetailList.size.toString())
                holder.detailOrderRec.layoutManager= LinearLayoutManager(nContext)
                val menuAdapter= KitchenTableDetailAdapter(nContext,detailList,detailListCount,orderDetailList,
                    holder.tableBtn,number)
                holder.detailOrderRec.adapter=menuAdapter

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
    }

    override fun getItemCount(): Int {
        return tableList.size
    }
    private fun selectswitch(orderDetailList:ArrayList<OrderDetailModel>){


    }

}