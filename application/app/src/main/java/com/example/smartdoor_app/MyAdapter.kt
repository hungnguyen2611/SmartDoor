package com.example.smartdoor_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(private val userList : ArrayList<record>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private lateinit var mListner : onItemClickListener


    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListner = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_item,
            parent,false)
        return MyViewHolder(itemView, mListner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = userList[position]
        holder.name.text = currentitem.fullname
        holder.date.text = currentitem.date
        Picasso.get().load(currentitem.url).resize(200,200).into(holder.url)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.nametv)
        val date : TextView = itemView.findViewById(R.id.datetv)
        val url : ImageView = itemView.findViewById(R.id.imageView2)

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

}