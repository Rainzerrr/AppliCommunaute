package com.example.applisondage.adapter

import android.util.Log.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.model.ActuModel
import com.example.applisondage.R


class ActuAdapter(val context : MainActivity, val actuList : List<ActuModel>) : RecyclerView.Adapter<ActuAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val actuImage = view.findViewById<ImageView>(R.id.actu_image)
        val actuDesc = view.findViewById<TextView>(R.id.actu_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_actus, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return actuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentActu = actuList[position]

        val imageName = currentActu.imageUrl
        holder.actuDesc.text = currentActu.desc

        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.actuImage.setImageResource(resourceId)
        } else {
            e("Error missing image","Image not found")
        }

    }
}
