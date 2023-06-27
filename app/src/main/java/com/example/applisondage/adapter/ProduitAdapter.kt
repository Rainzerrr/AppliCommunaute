package com.example.applisondage.adapter

import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.MainActivity.Companion.prodList
import com.example.applisondage.R
import com.example.applisondage.model.ActuModel
import com.example.applisondage.model.ProduitModel
import com.example.applisondage.model.SondageModel

import kotlinx.coroutines.processNextEventInCurrentThread

class ProduitAdapter(val context : MainActivity, val produitList : MutableList<ProduitModel>, val type : String,  val updateProgressBar: () -> Unit) : RecyclerView.Adapter<ProduitAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val produitName = view.findViewById<TextView>(R.id.alimentName)
        val produitTrash = view.findViewById<ImageView>(R.id.img_trash)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = if(type=="add")
            LayoutInflater.from(parent.context).inflate(R.layout.addproduit_layout, parent, false)
        else {
            LayoutInflater.from(parent.context).inflate(R.layout.consultproduit_layout, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return produitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == "add") {
            val currentProduit = produitList[position]
            holder.produitName.text = currentProduit.nomProd

            holder.itemView.setOnClickListener {
                if(produitsChoisis.size == 10)
                {
                    Toast.makeText(context, "Vous avez choisi le maximum de produit", Toast.LENGTH_SHORT).show()
                }
                else if(produitsChoisis.contains(currentProduit))
                {
                    Toast.makeText(context, "Ce produit a déjà été choisi", Toast.LENGTH_SHORT).show()
                }
                else{
                    produitsChoisis.add(currentProduit)
                    produitList.remove(currentProduit)
                    prodList.remove(currentProduit)
                    notifyDataSetChanged()
                    updateProgressBar.invoke()
                    Toast.makeText(context, currentProduit.nomProd + " ajouté", Toast.LENGTH_SHORT).show()
                }

            }
        } else if (type == "consult") {
            val currentProduit = produitList[position]
            holder.produitName.text = currentProduit.nomProd
            holder.produitTrash.setOnClickListener {
                produitsChoisis.remove(currentProduit)
                produitList.remove(currentProduit)
                prodList.add(currentProduit)
                notifyDataSetChanged()
                updateProgressBar.invoke()
                Toast.makeText(context, currentProduit.nomProd + " supprimé", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        val produitsChoisis = arrayListOf<ProduitModel>()
    }

}