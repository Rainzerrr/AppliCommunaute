package com.example.applisondage.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.applisondage.MainActivity
import com.example.applisondage.fragments.Maps
import com.example.applisondage.model.DecouvModel

import com.example.applisondage.R

class DecouvAdapter(val context : MainActivity, val decouvList : List<DecouvModel>) : RecyclerView.Adapter<DecouvAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val decouvImage = view.findViewById<ImageView>(R.id.decouv_image)
        val decouvDesc = view.findViewById<TextView>(R.id.decouv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_decouvrir, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return decouvList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDecouv = decouvList[position]
        holder.decouvDesc.text = currentDecouv.desc

        Glide.with(context).load(Uri.parse(currentDecouv.imageUrl)).into(holder.decouvImage)

        holder.itemView.setOnClickListener {
            val maps = Maps()
            maps.initEmplacement(currentDecouv.desc.toString())
            context.loadFragment(maps)
        }
    }

}