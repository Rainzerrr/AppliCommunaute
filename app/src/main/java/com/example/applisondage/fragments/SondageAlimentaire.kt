package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.adapter.ProduitAdapter

class SondageAlimentaire(val context : MainActivity) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sondage_alimentaire, container, false)

        val buttonAddProd = view?.findViewById<Button>(R.id.button_addProd)
        buttonAddProd?.setOnClickListener {
            context.loadFragment(SondageAddProd(context))
        }

        val buttonConsultProd = view?.findViewById<Button>(R.id.button_consultProd)
        buttonConsultProd?.setOnClickListener {
            context.loadFragment(SondageConsultProd(context, ProduitAdapter.getProdChoisis()))
        }

        val buttonReturn = view?.findViewById<Button>(R.id.returnButton5)
        buttonReturn?.setOnClickListener {
            context.loadFragment(Enregistrement2(context))
        }

        val buttonFinish = view?.findViewById<Button>(R.id.button_finish)
        buttonFinish?.setOnClickListener {
            ///ENVOYER REPONSES SONDAGES
        }

        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar3)
        progressBar?.progress = 80

        return view
    }
}