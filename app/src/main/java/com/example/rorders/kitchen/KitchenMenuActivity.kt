package com.example.rorders.kitchen

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
import com.example.rorders.admin.AdminOrdersActivity
import com.example.rorders.admin.adapter.CategoryAdapter
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.MenuDetailModel
import com.example.rorders.kitchen.adapter.KitchenMenuCategoryAdapter
import com.example.rorders.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class KitchenMenuActivity : AppCompatActivity() {
    lateinit var signOutBtn: Button
    lateinit var nContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var menuCatList: ArrayList<String>
    lateinit var menuTypeList: ArrayList<String>
    lateinit var menuTypeModel:ArrayList<MenuDetailModel>
    lateinit var itemListModel:ArrayList<ItemListModel>
    var menuItemNameList: MutableList<String?> = ArrayList()
    lateinit var menuItemUpdatedList:ArrayList<String>
    lateinit var item: ItemListModel
    lateinit var catRecView: RecyclerView

    var type:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen_menu)

        nContext = this
        auth = Firebase.auth
        db = Firebase.firestore

        init()
        menuUpdate()

    }


    private fun init() {
        signOutBtn = findViewById(R.id.signout_btn)
        catRecView = findViewById(R.id.cat_rec)
        menuCatList = ArrayList()
        menuTypeList = ArrayList()
        menuTypeModel= ArrayList()
        itemListModel=ArrayList<ItemListModel>()
        menuItemUpdatedList= ArrayList()

        signOutBtn.setOnClickListener {
           finish()
        }


    }
    private fun listing(){
        menuTypeModel= ArrayList()
        menuCatList= ArrayList()
        itemListModel= ArrayList()
        db.collection("categories").get().addOnSuccessListener {
            for (i in 0..it.documents.size - 1) {
                menuCatList.add(i, it.documents[i].id.toString())

            }


            for (i in menuCatList.indices) {

                var nmodel = MenuDetailModel(menuCatList[i], itemListModel,false)

                menuTypeModel.add(i, nmodel)

            }

            db.collection("Menu").get().addOnSuccessListener {
                for (j in menuTypeModel.indices) {
                    for (i in it.documents.indices) {
                        //Log.e("type", it.documents[i].get("item_name").toString())
                        if (it.documents[i].get("item_type") == menuTypeModel[j].type) {
                            var temp = ItemListModel(
                                it.documents[i].get("item_name").toString(),
                                it.documents[i].get("item_cost").toString(),
                                it.documents[i].get("item_type").toString()
                            )
                            menuTypeModel[j].detailList.add(temp)

                        }
                    }
                }

                Log.e("typelistsize",menuTypeModel.size.toString())
                catRecView.layoutManager = LinearLayoutManager(nContext)
                var categoryAdapter = KitchenMenuCategoryAdapter(nContext, menuTypeModel,menuItemNameList)
                catRecView.adapter = categoryAdapter
            }

        }


    }

    private fun menuUpdate(){
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
                Log.e("items selected",menuItemNameList.size.toString())
                listing()
                /* menuRecycler.layoutManager= LinearLayoutManager(nContext)
                 var menuAdapter= StaffMenuAdapter(nContext,menuItemNameList)
                 menuRecycler.adapter=menuAdapter*/
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancelled","true")
            }

        })
    }

}