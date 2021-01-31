package com.example.medlife_final

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*


class MedAdapter(private val MedList: ArrayList<Item>) : RecyclerView.Adapter<MedAdapter.MedViewHolder>(){

    class MedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(item: Item){
            itemView.NameShow.text = item.text1
            itemView.UseShow.text = item.text2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,
            parent,false)

        return MedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val currentItem = MedList[position]
        holder.itemView.NameShow.text = currentItem.text1
        holder.itemView.UseShow.text = currentItem.text1
        holder.bindItems(MedList[position])

        holder.itemView.setOnClickListener{
                val myIntent = Intent(holder.itemView.getContext(), DetailActivity::class.java)
                myIntent.putExtra("medicine",currentItem.text1)
                holder.itemView.getContext().startActivity(myIntent)
        }
    }

    override fun getItemCount() = MedList.size


}