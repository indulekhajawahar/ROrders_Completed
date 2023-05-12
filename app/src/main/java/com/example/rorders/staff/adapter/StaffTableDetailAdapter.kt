package com.example.rorders.staff.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.staff.model.OrderDetailModel

class StaffTableDetailAdapter(
    private var nContext: Context,
    private var detailList: MutableList<String>,
    private var detailListCount:MutableList<String>

) :

    RecyclerView.Adapter<StaffTableDetailAdapter.MyViewHolder>() {
    var number:String=""


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemname: TextView = view.findViewById(R.id.itemname)
        var itemcount: TextView = view.findViewById(R.id.item_count)
        var detailBtn:RadioButton=view.findViewById(R.id.detail_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_staff_detail_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.detailBtn.isClickable=false
        Log.e("detsize",detailList.size.toString())

       holder.itemname.text=detailList[position]
        holder.itemcount.text=detailListCount[position]
    }

    override fun getItemCount(): Int {
        return detailList.size
    }
}