package com.example.rorders.staff.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.example.rorders.R
import com.example.rorders.staff.model.OrderListModel
import com.example.rorders.staff.StaffOrderDetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StaffMenuAdapter (
    private var nContext: Context,
    private var menulist:MutableList<String?>,
    private var orderBtn:Button

) :

    RecyclerView.Adapter<StaffMenuAdapter.MyViewHolder>() {
    var isArrowClicked:Boolean=false
    lateinit var database: DatabaseReference
    lateinit var countArray:ArrayList<String>
    lateinit var orderArrayList:ArrayList<OrderListModel>
    lateinit var orderArrayListFirebase:ArrayList<OrderListModel>
    var countValue:Int=0

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.item)
        var count_min:ImageView=view.findViewById(R.id.count_sub)
        var count_max:ImageView=view.findViewById(R.id.count_add)
        var countNum:TextView=view.findViewById(R.id.count_num)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_staff_menu_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        countArray=ArrayList()
        for(i in menulist.indices){
            countArray.add(0.toString())
        }
        Log.e("count",countArray.size.toString())

      holder.itemName.setText(menulist[position])
        holder.countNum.setText(countArray[position])

        holder.count_min.setOnClickListener {
            if (holder.countNum.text=="0"){
                holder.countNum.setText("0")

            }else{
                countArray[position]= (countArray[position].toInt()-1).toString()
                holder.countNum.text= countArray[position].toString()
            }
        }
        holder.count_max.setOnClickListener {
            countArray[position]= (countArray[position].toInt()+1).toString()
            holder.countNum.text= countArray[position].toString()
        }
        orderArrayList= ArrayList()


        orderBtn.setOnClickListener {
            for(i in menulist.indices) {
                if(countArray[i]!="0") {
                    var nmodel = OrderListModel(menulist[i].toString(), countArray[i])
                    orderArrayList.add(nmodel)
                }
            }
            ordering(orderArrayList)
        }
    }
    override fun getItemCount(): Int {
        return menulist.size
    }
private fun ordering(orderArrayList: ArrayList<OrderListModel>){
    val dialog = BottomSheetDialog(nContext, R.style.AppBottomSheetDialogTheme)
    dialog.setContentView(R.layout.bottomsheet_order_register)
    dialog.setCancelable(true)
    var tableNum=dialog.findViewById<EditText>(R.id.table_name_edt)
    var submitButton = dialog.findViewById<Button>(R.id.submitButton)
    var cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
    cancelButton!!.setOnClickListener {
        dialog.dismiss()
    }
    submitButton!!.setOnClickListener {
        if (tableNum!!.text.isEmpty()){
            Toast.makeText(nContext, "Enter Table Number", Toast.LENGTH_SHORT).show()
        }else{
            addtorealtime(tableNum.text.toString(),orderArrayList)

            val i = Intent(nContext, StaffOrderDetailActivity::class.java)
           nContext.startActivity(i)
        }

    }


    dialog.show()
}
    private fun addtorealtime(tableNum:String,orderArray:ArrayList<OrderListModel>){
        database = FirebaseDatabase.getInstance().getReference("OrderDetails")

        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        for (i in orderArray.indices) {
            database.child("status").setValue("0")
            database.child(tableNum).child(orderArray[i].itemName).setValue(orderArray[i].itemCount)
            database.child(tableNum).child("status").setValue("0")
        }
    }

}