package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.adapter.ProduitAdapter
import com.example.applisondage.model.ProduitModel

class SondageConsultProd(val context : MainActivity, private val prodList : List<ProduitModel>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sondage_consult_produit, container, false)

        val buttonReturn = view?.findViewById<Button>(R.id.returnButton7)
        buttonReturn?.setOnClickListener {
            context.loadFragment(SondageAlimentaire(context))
        }



        val verticalRV = view?.findViewById<RecyclerView>(R.id.rv_consultproduit)
        verticalRV?.adapter = ProduitAdapter(context, prodList, "consult", ::updateProgressBar)
        verticalRV?.layoutManager = LinearLayoutManager(context)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar4)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10


        return view
    }

    private fun updateProgressBar() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar4)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10
    }


}