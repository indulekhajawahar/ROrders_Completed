package com.example.rorders.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rorders.R
import com.example.rorders.admin.model.ItemListModel
import com.example.rorders.admin.model.MenuDetailModel
import org.checkerframework.checker.nullness.qual.NonNull

class CategoryAdapter  (
    private var mContext: Context,
    private var categoryArrayList: ArrayList<MenuDetailModel>,
    var menuUpdateList: MutableList<String?>
) :

    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    //var isArrowClicked:Boolean=false
    lateinit var itemList:ArrayList<String>
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryNameTxt: TextView = view.findViewById(R.id.cat_name)
        var linearMain:LinearLayout=view.findViewById(R.id.linear_main)
        var arrowUp:ImageView=view.findViewById(R.id.arrow_up)
        var arrowDown:ImageView=view.findViewById(R.id.arrow_down)
        var itemsRecycler:RecyclerView=view.findViewById(R.id.items_rec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
Log.e("menuitemlist cat",menuUpdateList.size.toString())
        itemList= ArrayList()
        holder.itemsRecycler.visibility=View.VISIBLE


        holder.categoryNameTxt.text = categoryArrayList[position].type
        for (i in categoryArrayList[position].detailList.indices){
            if (categoryArrayList[position].type==categoryArrayList[position].detailList[i].itemType){
                if (itemList.contains(categoryArrayList[position].detailList[i].itemName)){
                    Log.e("item","already exist")
                }else {
                    itemList.add(categoryArrayList[position].detailList[i].itemName)
                }
            }
        }
Log.e("itemlistsize",itemList.size.toString())
        holder.itemsRecycler.layoutManager=LinearLayoutManager(mContext)
        var adapter=ItemsAdapter(mContext,categoryArrayList[position].detailList,
            itemList,menuUpdateList)
        holder.itemsRecycler.adapter=adapter

        /*holder.itemView.setOnClickListener {
            Log.e("arow",categoryArrayList[position].isArrowClicked.toString())
            if (categoryArrayList[position].isArrowClicked==false){
                Log.e("arow","false")
                categoryArrayList[position].isArrowClicked=true
                itemList=ArrayList()
                holder.arrowDown.visibility=View.GONE
                holder.arrowUp.visibility=View.VISIBLE
                holder.itemsRecycler.visibility=View.VISIBLE
                holder.itemsRecycler.layoutManager=LinearLayoutManager(mContext)
                for (i in categoryArrayList[position].detailList.indices){
                    if (categoryArrayList[position].type==categoryArrayList[position].detailList[i].itemType){
                        itemList.add(categoryArrayList[position].detailList[i].itemName)
                    }
                }
                var adapter=ItemsAdapter(mContext,categoryArrayList[position].detailList,
                    itemList,menuBtn)
                holder.itemsRecycler.adapter=adapter
            }else{
                Log.e("arow","true")
                categoryArrayList[position].isArrowClicked=false
                holder.arrowDown.visibility=View.VISIBLE
                holder.arrowUp.visibility=View.GONE
                holder.itemsRecycler.visibility=View.GONE
            }


        }*/

    }


    override fun getItemCount(): Int {
        return categoryArrayList.size
    }


}