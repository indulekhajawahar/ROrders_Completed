package com.example.rorders.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.adapter.CategoryAdapter
import com.example.rorders.admin.adapter.FilteredItemAdapter
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.ItemStatusModel
import com.example.rorders.admin.model.ItemStatusModelNew
import com.example.rorders.admin.model.MenuDetailModel
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
import java.util.*
import kotlin.collections.ArrayList


class AdminMainActivity : AppCompatActivity() {
    lateinit var signOutBtn: Button
    lateinit var nContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var itemList:ArrayList<String>
    lateinit var typeList:ArrayList<String>
    lateinit var itemFilteredList:ArrayList<ItemStatusModel>
    lateinit var menuCatList: ArrayList<String>
    lateinit var menuTypeList: ArrayList<String>
    lateinit var menuTypeModel:ArrayList<MenuDetailModel>
    lateinit var filtered:ArrayList<ItemStatusModelNew>
    lateinit var itemListModel:ArrayList<ItemListModel>
    var menuItemNameList: MutableList<String?> = ArrayList()
    lateinit var menuItemUpdatedList:ArrayList<String>
    lateinit var item:ItemListModel
    lateinit var catRecView: RecyclerView
    lateinit var filterRecView: RecyclerView
    lateinit var addButton: FloatingActionButton
    lateinit var menuButton: Button
    lateinit var searchtxt: EditText
    lateinit var searchbtn: ImageView
    var doubleBackToExitPressedOnce:Boolean= false
    var backPressedTime: Long = 0
    var type:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show()
        nContext = this
        auth = Firebase.auth
        db = Firebase.firestore

        init()
        menuUpdate()
        searchbtn.setOnClickListener(View.OnClickListener {
            if(searchtxt.text.isEmpty()){
                catRecView.visibility=View.VISIBLE
                filterRecView.visibility=View.GONE
            }else {
                catRecView.visibility = View.GONE
                filterRecView.visibility = View.VISIBLE

                catRecView.visibility = View.GONE
                filterRecView.visibility = View.VISIBLE
                Log.e("search", "clicked")
                filtered = ArrayList()
                if (itemList.size > 0) {
                    filtered = ArrayList<ItemStatusModelNew>()

                    Log.e("itemsize", itemList.size.toString())
                    filtered = ArrayList()
                    for (i in itemList.indices) {
                        if (itemList.get(i)
                                .lowercase(Locale.getDefault())
                                .contains(searchtxt.text.toString().lowercase(Locale.getDefault()))
                        ) {
                            var nmodel = ItemStatusModelNew(itemList[i],typeList[i], "0")
                            filtered.add(nmodel)
                        }
                    }
                    for (j in menuItemNameList.indices) {
                        for (k in filtered.indices) {
                            if (menuItemNameList[j] == filtered[k].itemName) {
                                filtered[k].status = "1"
                            }
                        }
                    }
                    itemFilteredList = ArrayList()
                    for (i in 0..filtered.size - 1) {
                        for (j in 0..itemFilteredList.size - 1) {
                            if (filtered[i].itemName == itemFilteredList[j].itemName) {
                                var nmodel =
                                    ItemStatusModel(filtered[i].itemName, filtered[i].status)
                                itemFilteredList.add(nmodel)
                            }
                        }
                    }
                    Log.e("filtersize", itemFilteredList.size.toString())
                    filterRecView.layoutManager = LinearLayoutManager(nContext)
                    var cat_adapter = FilteredItemAdapter(nContext, filtered)
                    filterRecView.adapter = cat_adapter

                }
            }

        })
        searchtxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if(searchtxt.text.isEmpty()){
                    catRecView.visibility=View.VISIBLE
                    filterRecView.visibility=View.GONE
                }else {
                    catRecView.visibility = View.GONE
                    filterRecView.visibility = View.VISIBLE


                    Log.e("search", "clicked")
                    filtered = ArrayList()
                    if (itemList.size > 0) {
                        filtered = ArrayList<ItemStatusModelNew>()

                        Log.e("itemsize", itemList.size.toString())
                        filtered = ArrayList()
                        for (i in itemList.indices) {
                            if (itemList.get(i)
                                    .lowercase(Locale.getDefault())
                                    .contains(
                                        searchtxt.text.toString().lowercase(Locale.getDefault())
                                    )
                            ) {
                                var nmodel = ItemStatusModelNew(itemList[i], typeList[i],"0")
                                filtered.add(nmodel)
                            }
                        }
                        for (j in menuItemNameList.indices) {
                            for (k in filtered.indices) {
                                if (menuItemNameList[j] == filtered[k].itemName) {
                                    filtered[k].status = "1"
                                }
                            }
                        }
                        itemFilteredList = ArrayList()
                        for (i in 0..filtered.size - 1) {
                            for (j in 0..itemFilteredList.size - 1) {
                                if (filtered[i].itemName == itemFilteredList[j].itemName) {
                                    var nmodel =
                                        ItemStatusModel(filtered[i].itemName, filtered[i].status)
                                    itemFilteredList.add(nmodel)
                                }
                            }
                        }
                        Log.e("filtersize", itemFilteredList.size.toString())
                        filterRecView.layoutManager = LinearLayoutManager(nContext)
                        var cat_adapter = FilteredItemAdapter(nContext, filtered)
                        filterRecView.adapter = cat_adapter

                    }
                }

            }


            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        /*searchbtn.setOnClickListener(View.OnClickListener {

            Log.e("search","clicked")
            menuTypeModel= ArrayList()
            menuCatList= ArrayList()
            itemListModel= ArrayList()
            db.collection("categories").get().addOnSuccessListener {
                for (i in 0..it.documents.size - 1) {

                        if (it.documents[i].id
                                .lowercase(Locale.getDefault())
                                .contains(searchtxt.text.toString().lowercase(Locale.getDefault())) || it.documents[i].id
                                .lowercase(Locale.getDefault())
                                .contains(searchtxt.text.toString().lowercase(Locale.getDefault()))
                        ) {
                            menuCatList.add(it.documents[i].id.toString())
                           // filtered.add(menuTypeModel.get(i))
                        }


                }
                for (i in menuCatList.indices) {

                    var nmodel = MenuDetailModel(menuCatList[i], itemListModel,false)

                    filtered.add(i, nmodel)

                }

                db.collection("Menu").get().addOnSuccessListener {
                    for (j in filtered.indices) {
                        for (i in it.documents.indices) {
                            //Log.e("type", it.documents[i].get("item_name").toString())
                            if (it.documents[i].get("item_type") == filtered[j].type) {
                                var temp = ItemListModel(
                                    it.documents[i].get("item_name").toString(),
                                    it.documents[i].get("item_cost").toString(),
                                    it.documents[i].get("item_type").toString()
                                )
                                filtered[j].detailList.add(temp)

                            }
                        }
                    }

                    Log.e("typelistsize",menuTypeModel.size.toString())
                    catRecView.layoutManager = LinearLayoutManager(nContext)
                    var categoryAdapter = CategoryAdapter(nContext, filtered,menuItemNameList)
                    catRecView.adapter = categoryAdapter
                }

            }

        })
        searchtxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                menuTypeModel= ArrayList()
                menuCatList= ArrayList()
                itemListModel= ArrayList()
                filtered=ArrayList()
                db.collection("categories").get().addOnSuccessListener {
                    for (i in 0..it.documents.size - 1) {

                        if (it.documents[i].id
                                .lowercase(Locale.getDefault())
                                .contains(searchtxt.text.toString().lowercase(Locale.getDefault())) || it.documents[i].id
                                .lowercase(Locale.getDefault())
                                .contains(searchtxt.text.toString().lowercase(Locale.getDefault()))
                        ) {
                            menuCatList.add(it.documents[i].id.toString())
                            // filtered.add(menuTypeModel.get(i))
                        }


                    }
                    for (i in menuCatList.indices) {

                        var nmodel = MenuDetailModel(menuCatList[i], itemListModel,false)

                        filtered.add(i, nmodel)

                    }

                    db.collection("Menu").get().addOnSuccessListener {
                        for (j in filtered.indices) {
                            for (i in it.documents.indices) {
                                //Log.e("type", it.documents[i].get("item_name").toString())
                                if (it.documents[i].get("item_type") == filtered[j].type) {
                                    var temp = ItemListModel(
                                        it.documents[i].get("item_name").toString(),
                                        it.documents[i].get("item_cost").toString(),
                                        it.documents[i].get("item_type").toString()
                                    )
                                    filtered[j].detailList.add(temp)

                                }
                            }
                        }

                        Log.e("typelistsize",menuTypeModel.size.toString())
                        catRecView.layoutManager = LinearLayoutManager(nContext)
                        var categoryAdapter = CategoryAdapter(nContext, filtered,menuItemNameList)
                        catRecView.adapter = categoryAdapter
                    }

                }

                }


            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })*/

    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


    private fun init() {
        signOutBtn = findViewById(R.id.signout_btn)
        catRecView = findViewById(R.id.cat_rec)
        filterRecView = findViewById(R.id.filter_rec)
        addButton = findViewById(R.id.add_btn)
        menuButton = findViewById(R.id.menu_btn)
        searchbtn = findViewById(R.id.btnImgsearch)
        searchtxt = findViewById(R.id.searchEditText)
        itemList= ArrayList()
        itemFilteredList= ArrayList()
        menuCatList = ArrayList()
        menuTypeList = ArrayList()
        menuTypeModel= ArrayList()
        typeList= ArrayList()
        filtered= ArrayList()
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
            var intent = Intent(nContext, AdminOrdersActivity::class.java)
            startActivity(intent)
        }
        /* var firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
         //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
         firebaseDatabase.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {

                 Log.e("datchange","true")
                 menuItemNameList = ArrayList()
                 for (ds in dataSnapshot.children) {
                     val menuItem = ds.key
                     menuItemNameList.add(menuItem.toString())

                     Log.e("TAG", menuItem.toString())

                 }
                 Log.e("listchange",menuItemNameList.size.toString())
                for (i in menuItemNameList.indices){
                    menuItemUpdatedList.add(menuItemNameList[i].toString())
                }
                 AppController.menuArrayList.addAll(menuItemUpdatedList)
                 Log.e("listchangeupdate",menuItemUpdatedList.size.toString())
                 catRecView.layoutManager = LinearLayoutManager(nContext)
                 var categoryAdapter = CategoryAdapter(nContext, menuTypeModel,menuItemNameList)
                 catRecView.adapter = categoryAdapter
             }
             override fun onCancelled(error: DatabaseError) {
                 Log.e("cancelled","true")
             }

         })*/
    }
    private fun listing(){
        itemList= ArrayList()
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
                itemList= ArrayList()
                typeList= ArrayList()
                for (j in menuTypeModel.indices) {
                    for (i in it.documents.indices) {
                        var item=it.documents[i].get("item_name")
                        var type=it.documents[i].get("item_type")
if (itemList.contains(item)){
    Log.e("item","ex")
}else{
    itemList.add(item.toString())
    typeList.add(type.toString())

}
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
                var categoryAdapter = CategoryAdapter(nContext, menuTypeModel,menuItemNameList)
                catRecView.adapter = categoryAdapter
            }

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
                        menuUpdate()
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.e("error", "Error adding document", e)
                    }
            }

        }

        dialog.show()
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
   /* lateinit var signOutBtn: Button
    lateinit var nContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var menuCatList: ArrayList<String>
    lateinit var menuTypeList: ArrayList<String>
    lateinit var menuTypeModel:ArrayList<MenuDetailModel>
    lateinit var itemListModel:ArrayList<ItemListModel>
    lateinit var item:ItemListModel
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
        selection()

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
        db.collection("categories").get().addOnSuccessListener {
            for (i in 0..it.documents.size - 1) {
                menuCatList.add(i, it.documents[i].id.toString())
                *//*itemListModel= ArrayList()
                Log.e("cat",menuCatList[i])
                    db.collection("categories/"+menuCatList[i]+"/items").get().addOnSuccessListener{

                        for (j in it.documents.indices) {

                            var temp = ItemListModel(
                                it.documents[j].get("item_name").toString(),
                                it.documents[j].get("item_cost").toString()
                            )
                            Log.e("itemsname",temp.itemName)
                            itemListModel.add(temp)

                        }

                    }
                var nmodel=MenuDetailModel(menuCatList[i],itemListModel)
                menuTypeModel.add(i,nmodel)*//*

            }


            for (i in menuCatList.indices) {
                var nmodel = MenuDetailModel(menuCatList[i], itemListModel,false)
                menuTypeModel.add(i, nmodel)
                Log.e("types", menuTypeModel[i].type)
            }

            *//*for (i in it.documents.indices) {
                    var temp = ItemListModel(
                        it.documents[i].get("item_name").toString(),
                        it.documents[i].get("item_cost").toString(),
                        it.documents[i].get("item_type").toString()
                    )
                   itemListModel.add(temp)
                }*//*
            *//* for (i in menuCatList.indices){
                    for (j in itemListModel.indices){
                        if (menuCatList[i]==itemListModel[j].itemType){


                        }
                    }
                }*//*

            db.collection("Menu").get().addOnSuccessListener {
                for (j in menuTypeModel.indices) {
                    for (i in it.documents.indices) {
                        Log.e("type", it.documents[i].get("item_name").toString())
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
            }


            catRecView.layoutManager = LinearLayoutManager(nContext)
            var categoryAdapter = CategoryAdapter(nContext, menuTypeModel,menuButton)
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
    private fun selection(){

    }*/
}