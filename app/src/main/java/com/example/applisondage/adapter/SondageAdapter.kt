package com.example.applisondage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.model.CurrentUserProvider
import com.example.applisondage.model.SondageModel

class SondageAdapter(val context : MainActivity, val sondageList : List<SondageModel>) : RecyclerView.Adapter<SondageAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val sondageSujet = view.findViewById<TextView>(R.id.sondage_sujet)
        val sondageDuree = view.findViewById<TextView>(R.id.sondage_duree)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sondages_sujets_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sondageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentSondage = sondageList[position]
        holder.sondageSujet.text = currentSondage.sujet
        holder.sondageDuree.text = currentSondage.duree
        holder.itemView.setOnClickListener{
            if(CurrentUserProvider.currentUser == null)
            {
                Toast.makeText(context, "Veuillez vous connecter pour accéder aux sondages", Toast.LENGTH_SHORT).show()
            }
            else{
                context.loadFragment(currentSondage.sondage)
            }

        }
    }

}