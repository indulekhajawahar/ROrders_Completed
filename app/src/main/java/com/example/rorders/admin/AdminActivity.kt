package com.example.rorders.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.adapter.CategoryAdapter
import com.example.rorders.admin.adapter.CategoryAdapterNew
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.MenuDetailModel
import com.example.rorders.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminActivity:AppCompatActivity() {
    lateinit var signOutBtn: Button
    lateinit var nContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var menuCatList: ArrayList<String>
    lateinit var menuTypeList: ArrayList<String>
    lateinit var menuTypeModel:ArrayList<String>
    lateinit var itemListModel:ArrayList<ItemListModel>
    var menuItemNameList: MutableList<String?> = ArrayList()
    lateinit var menuItemUpdatedList:ArrayList<String>
    lateinit var item: ItemListModel
    lateinit var catRecView: RecyclerView
    lateinit var addButton: FloatingActionButton
    lateinit var menuButton: Button
    var type:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show()
        nContext = this
        auth = Firebase.auth
        db = Firebase.firestore

        init()
        listing()
        //menuUpdate()



    }
    private fun init() {
        signOutBtn = findViewById(R.id.signout_btn)
        catRecView = findViewById(R.id.cat_rec)
        addButton = findViewById(R.id.add_btn)
        menuButton = findViewById(R.id.menu_btn)
        menuCatList = ArrayList()
        menuTypeList = ArrayList()
        menuTypeModel= ArrayList()
        itemListModel=ArrayList<ItemListModel>()
        menuItemUpdatedList= ArrayList()

        signOutBtn.setOnClickListener {
            auth.signOut()
            var intent = Intent(nContext, LoginActivity::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            addToMenu()
        }
        menuButton.setOnClickListener {

        }
    }
    private fun listing(){
        menuTypeModel= ArrayList()
        db.collection("MENU").get().addOnSuccessListener {
            for (i in 0..it.documents.size - 1) {
                menuTypeModel.add(i, it.documents[i].id.toString())

            }
            Log.e("typelistsize",menuTypeModel.size.toString())
                catRecView.layoutManager = LinearLayoutManager(nContext)
                var categoryAdapter = CategoryAdapterNew(nContext, menuTypeModel)
                catRecView.adapter = categoryAdapter


        }
    }
    private fun addToMenu(){
        val dialog = BottomSheetDialog(nContext, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottomsheet_add_item, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        var itemName = dialog.findViewById<EditText>(R.id.itemname_edt)
        var itemCost = dialog.findViewById<EditText>(R.id.itemcost_edt)
        var spinner = dialog.findViewById<Spinner>(R.id.spinnerType)
        var submitButton = dialog.findViewById<Button>(R.id.submitButton)
        var cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        var  selectedType:String=""

        cancelButton!!.setOnClickListener {
            dialog.dismiss()
        }
        menuTypeList=ArrayList()
        menuTypeList.add(0,"Select Type")
        for (i in 1..menuCatList.size){
            menuTypeList.add(i,menuCatList[i-1])
        }
        if(spinner!=null) {
            var sp_adapter = ArrayAdapter(
                nContext,
                R.layout.spinner_textview, menuTypeList)
            spinner.adapter = sp_adapter
            spinner.setSelection(0)

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedType = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }
        submitButton!!.setOnClickListener {
            var itemNameEntered=itemName!!.text.toString()
            var itemCostEntered=itemCost!!.text.toString()

            if (itemNameEntered.isEmpty()){
                Toast.makeText(nContext,"Enter item name", Toast.LENGTH_SHORT).show()
            }else if (itemCostEntered.isEmpty()){
                Toast.makeText(nContext,"Enter item cost", Toast.LENGTH_SHORT).show()
            }else if (selectedType.equals("Select Type")){
                Toast.makeText(nContext,"Select item type ", Toast.LENGTH_SHORT).show()
            }else{


                val user = hashMapOf(
                    "item_cost" to itemCostEntered,
                    "item_name" to itemNameEntered,
                    "item_type" to selectedType
                )

                // Add a new document with a generated ID
                db.collection("Menu")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(nContext, "Item added successfully", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.e("error", "Error adding document", e)
                    }
            }

        }

        dialog.show()
    }
}