package com.example.rorders.kitchen.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.kitchen.model.TableListModel
import com.example.rorders.staff.adapter.StaffTableDetailAdapter
import com.example.rorders.staff.model.OrderDetailModel
import com.example.rorders.staff.model.OrderListModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class KitchenTableDetailAdapter (
    private var nContext: Context,
    private var detailList: MutableList<String>,
    private var detailListCount:MutableList<String>,
    private var orderDetailList:ArrayList<OrderDetailModel>,
    private var tableBtn:Switch,
    var number:String

) :

    RecyclerView.Adapter<KitchenTableDetailAdapter.MyViewHolder>() {



    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemname: TextView = view.findViewById(R.id.itemname)
        var itemcount: TextView = view.findViewById(R.id.item_count)
        var detailBtn: RadioButton =view.findViewById(R.id.detail_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_staff_detail_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.detailBtn.isClickable=true
        var tables: OrderDetailModel = orderDetailList.get(position)

        holder.itemname.text=orderDetailList[position].itemName
        holder.itemcount.text=orderDetailList[position].itemCount

        if (tables.status == "1")
        {
            tableBtn.isChecked = true

        } else
        {
           tableBtn.isChecked = false

        }
        tableBtn.setOnCheckedChangeListener{ buttonView, isChecked ->

            /*database= FirebaseDatabase.getInstance().getReference("StudentLists").
            child(employee_array.get(position).name.toString()).child("status")*/
            var database: DatabaseReference
            if (isChecked)
            {
                database = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number)
                database.child("status").setValue("1")
                Log.e("Checked_Success",isChecked.toString())

                tableBtn.isChecked = true

                /*database.setValue("0")
                        holder.switch_button.isChecked=true*/

            }
            else
            {
                database = FirebaseDatabase.getInstance().getReference("OrderDetails").child(number)
                database.child("status").setValue("0")
                Log.e("checked_error",isChecked.toString())

                tableBtn.isChecked = false
                /*database.setValue("1")
                            holder.switch_button.isChecked=false*/

            }


        }

    }

    override fun getItemCount(): Int {
        return orderDetailList.size
    }
}