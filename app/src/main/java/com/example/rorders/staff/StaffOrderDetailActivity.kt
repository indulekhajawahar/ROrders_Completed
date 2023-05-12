package com.example.rorders.staff

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.kitchen.model.TableListModel
import com.example.rorders.staff.adapter.StaffMenuAdapter
import com.example.rorders.staff.adapter.StaffTableAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffOrderDetailActivity:AppCompatActivity() {
    lateinit var nContext: Context
    lateinit var heading:TextView
    lateinit var backBtn:Button
    lateinit var detailTableRec:RecyclerView
    var tableList: ArrayList<TableListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_detail)
        nContext=this

        init()
        orderdetail()

    }
    private fun init(){
        heading=findViewById(R.id.heading)
        heading.text="Order Detais"
        backBtn=findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            val i = Intent(nContext, StaffMainActivity::class.java)
            startActivity(i)
        }
        detailTableRec=findViewById(R.id.detail_table_rec)

    }
    private fun orderdetail(){
        var firebaseDatabase = FirebaseDatabase.getInstance().getReference("OrderDetails")
        //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*val value: String? = dataSnapshot.getValue(String::class.java)
                Log.d("TAG", value.toString())*/
                Log.e("datchange","true")
                tableList = ArrayList()
                for (ds in dataSnapshot.children) {
                    var menuItem = ds.key
                    var tableNum:String=""
                    var status=ds.child("status").value.toString()
                    if(menuItem.toString()!="status"){
                        var nmodel= TableListModel(menuItem.toString(),status)
                        tableList.add(nmodel)
                    }


                    Log.e("TAG", menuItem.toString())
                }
              /*  for (ds in dataSnapshot.children) {
                    val menuItem = ds.key
                    if (menuItem.toString()=="status"){
                        Log.e("st","st")
                    }else{

                        tableList.add(menuItem.toString())

                        Log.e("TAG", menuItem.toString())
                    }

                }*/
                Log.e("listchange",tableList.size.toString())
                detailTableRec.layoutManager= LinearLayoutManager(nContext)
                val menuAdapter= StaffTableAdapter(nContext,tableList)
                detailTableRec.adapter=menuAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
    }
}