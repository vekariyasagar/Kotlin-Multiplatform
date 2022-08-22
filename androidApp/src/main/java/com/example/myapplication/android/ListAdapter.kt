package com.example.myapplication.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.cache.Hello

class ListAdapter(var dataList : List<Hello>, var selectedItem: SelectedItem) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvProject: TextView = view.findViewById(R.id.tvProject)

        val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvDate.text = "Date : " + dataList[position].date;
        holder.tvName.text = "Name : " + dataList[position].name;
        holder.tvProject.text = "Project : " + dataList[position].project;

        holder.ivEdit.setOnClickListener (object: View.OnClickListener{
            override fun onClick(p0: View?) {
                selectedItem.edit(position)
            }
        })

        holder.ivDelete.setOnClickListener (object: View.OnClickListener{
            override fun onClick(p0: View?) {
                selectedItem.delete(position)
            }
        })

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}