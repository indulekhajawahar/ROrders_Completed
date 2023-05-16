package com.example.rorders.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.model.MenuDetailModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryAdapterNew  (
    private var mContext: Context,
    private var categoryArrayList: ArrayList<String>
) :

    RecyclerView.Adapter<CategoryAdapterNew.MyViewHolder>() {
    //var isArrowClicked:Boolean=false
    private lateinit var db: FirebaseFirestore

    lateinit var itemList:ArrayList<String>
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryNameTxt: TextView = view.findViewById(R.id.cat_name)
        var linearMain: LinearLayout =view.findViewById(R.id.linear_main)

        var itemsRecycler: RecyclerView =view.findViewById(R.id.items_rec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        db = Firebase.firestore
        itemList= ArrayList()
        holder.itemsRecycler.visibility= View.VISIBLE


        holder.categoryNameTxt.text = categoryArrayList[position]


        db.collection("MENU").get().addOnSuccessListener {

            for (i in 0..it.documents.size - 1) {
                //menuTypeModel.add(i, it.documents[i].id.toString())
                if (it.documents[i].id==categoryArrayList[position]){
                    Log.e("itvalue",it.documents[i].data.toString())
                    Log.e("itvalue",it.documents[i].id.toString())

                }

            }
            /*Log.e("typelistsize",menuTypeModel.size.toString())
            catRecView.layoutManager = LinearLayoutManager(nContext)
            var categoryAdapter = CategoryAdapterNew(nContext, menuTypeModel)
            catRecView.adapter = categoryAdapter*/


        }


    }


    override fun getItemCount(): Int {
        return categoryArrayList.size
    }


}