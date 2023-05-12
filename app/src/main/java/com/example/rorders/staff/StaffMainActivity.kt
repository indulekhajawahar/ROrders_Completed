package com.example.rorders.staff

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.login.LoginActivity
import com.example.rorders.staff.adapter.StaffMenuAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class StaffMainActivity : AppCompatActivity() {
    lateinit var nContext: Context
    private lateinit var auth: FirebaseAuth
    lateinit var signOutBtn: Button
    lateinit var heading: TextView
    lateinit var menuRecycler: RecyclerView
    lateinit var newOrderBtn:Button
    lateinit var viewOrdersBtn:Button
    lateinit var menuList:ArrayList<String>
    var menuItemNameList: MutableList<String?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_main)
        nContext=this
        auth = Firebase.auth
        init()
        listing()
        Toast.makeText(this, "Staff", Toast.LENGTH_SHORT).show()
    }
    private fun init(){
        menuList= ArrayList()
        signOutBtn=findViewById(R.id.signout_btn)
        heading=findViewById(R.id.heading)
        heading.setText("Todays Menu")
        menuRecycler=findViewById(R.id.menu_rec)
        newOrderBtn=findViewById(R.id.new_order)
        viewOrdersBtn=findViewById(R.id.view_orders)
        signOutBtn.setOnClickListener {
            auth.signOut()
            var intent= Intent(nContext, LoginActivity::class.java)
            startActivity(intent)

        }
        viewOrdersBtn.setOnClickListener {
            var intent= Intent(nContext, StaffOrderDetailActivity::class.java)
            startActivity(intent)
        }
       /* orderBtn.setOnClickListener {
            ordering()
        }*/

    }
    private fun listing(){

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
                    menuItemNameList.add(menuItem.toString())

                    Log.e("TAG", menuItem.toString())
                    /* Log.e("listchange",menuItemNameList.size.toString())
                     menuRecycler.layoutManager=LinearLayoutManager(nContext)
                     var menuAdapter= StaffMenuAdapter(nContext,menuItemNameList)
                     menuRecycler.adapter=menuAdapter*/
                }
                Log.e("listchange",menuItemNameList.size.toString())
                menuRecycler.layoutManager= LinearLayoutManager(nContext)
                val menuAdapter= StaffMenuAdapter(nContext,menuItemNameList,newOrderBtn)
                menuRecycler.adapter=menuAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
        /* Log.e("listad",menuItemNameList.size.toString())
         menuRecycler.layoutManager=LinearLayoutManager(nContext)
         var menuAdapter= StaffMenuAdapter(nContext,menuItemNameList)
         menuRecycler.adapter=menuAdapter*/
    }
    private fun ordering(){
        val dialog = BottomSheetDialog(nContext, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottomsheet_order_register, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        var tableNum=dialog.findViewById<EditText>(R.id.table_name_edt)
        var submitButton = dialog.findViewById<Button>(R.id.submitButton)
        var cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        cancelButton!!.setOnClickListener {
            dialog.dismiss()
        }
        submitButton!!.setOnClickListener {
            if (tableNum!!.text.isEmpty()){
                Toast.makeText(this, "Enter Table Number", Toast.LENGTH_SHORT).show()
            }else{
                val i = Intent(nContext, StaffOrderDetailActivity::class.java)
                startActivity(i)
            }

        }


        dialog.show()

    }

}