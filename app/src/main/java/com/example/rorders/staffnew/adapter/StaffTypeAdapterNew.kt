package com.example.rorders.staffnew.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.model.ItemStatusModel
import com.example.rorders.staff.adapter.StaffMenuAdapter
import com.example.rorders.staff.adapter.StaffTypeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffTypeAdapterNew (
    private var mContext: Context,
    private var categoryArrayList: MutableList<String?>,
    var newOrdrBtn: Button
) :

    RecyclerView.Adapter<StaffTypeAdapterNew.MyViewHolder>() {
    //var isArrowClicked:Boolean=false
    lateinit var itemList:MutableList<String?>
    lateinit var itemTypeList:ArrayList<String>
    lateinit var menuItemNameList:MutableList<String?>
    lateinit var itemNameList:ArrayList<ItemStatusModel>
    lateinit var firebaseDatabase: DatabaseReference
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryNameTxt: TextView = view.findViewById(R.id.cat_name)
        var linearMain: LinearLayout =view.findViewById(R.id.linear_main)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category_list_new, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        itemList= ArrayList()
        itemTypeList= ArrayList()
        menuItemNameList= ArrayList()
        itemNameList= ArrayList()


        holder.categoryNameTxt.text = categoryArrayList[position]

        holder.linearMain.setOnClickListener {
            var firebaseDatabase = FirebaseDatabase.getInstance().getReference("TodaysMenu")
            //var databaseReference = firebaseDatabase.getReference("TodaysMenu");
            firebaseDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                            Log.e("datchange","true")
                    menuItemNameList = ArrayList()
                    for (ds in dataSnapshot.children) {
                        val menuItem = ds.key
                        var type=ds.value
                        itemList.add(menuItem.toString())
                        if(categoryArrayList[position]==type){
                            menuItemNameList.add(menuItem.toString())

                        }else{
                            Log.e("item","con")
                        }


                        Log.e("TAG", menuItem.toString())

                    }
                    itemListPopUp(categoryArrayList[position].toString())

                    /*Log.e("listchange",menuItemNameList.size.toString())
                    holder.itemsRecycler.layoutManager= LinearLayoutManager(mContext)
                    val menuAdapter= StaffMenuAdapter(mContext,menuItemNameList,newOrdrBtn,itemList)
                    holder.itemsRecycler.adapter=menuAdapter*/
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancelled","true")
                }

            })
        }

   /*    */
        /* Log.e("menuItemNameList",menuItemNameList.size.toString())
         holder.itemsRecycler.layoutManager= LinearLayoutManager(mContext)
         var adapter= ItemsAdapter(mContext,categoryArrayList[position].detailList,
             itemList,itemNameList,itemTypeList)
         holder.itemsRecycler.adapter=adapter*/


    }


    override fun getItemCount(): Int {
        return categoryArrayList.size
    }
private fun itemListPopUp(type:String){
    val dialog = Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog)
   // val view = View.layoutInflater.inflate(R.layout.bottomsheet_item, null)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    dialog.setContentView(R.layout.bottomsheet_item)
    dialog.setCancelable(true)
    var itemsRecycler: RecyclerView? =dialog.findViewById(R.id.items_rec)
    var heading: TextView? =dialog.findViewById(R.id.heading)
    var closeBtn: ImageView? =dialog.findViewById(R.id.close_btn)
    /*var itemName: TextView? = dialog.findViewById(R.id.item)
    var count_min: ImageView? =dialog.findViewById(R.id.count_sub)
    var count_max: ImageView? =dialog.findViewById(R.id.count_add)
    var countNum:TextView?=dialog.findViewById(R.id.count_num)*/
heading!!.text=type
    closeBtn!!.setOnClickListener {
        dialog.dismiss()
    }
    Log.e("listchange",menuItemNameList.size.toString())
    itemsRecycler!!.layoutManager= LinearLayoutManager(mContext)
    val menuAdapter= StaffMenuAdapter(mContext,menuItemNameList,newOrdrBtn,itemList)
    itemsRecycler.adapter=menuAdapter


    dialog.show()
}

}