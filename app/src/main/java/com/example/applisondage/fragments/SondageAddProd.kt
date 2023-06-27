package com.example.applisondage.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.MainActivity.Companion.prodList
import com.example.applisondage.R
import com.example.applisondage.adapter.ProduitAdapter
import com.example.applisondage.adapter.SondageAdapter
import com.example.applisondage.model.DecouvModel
import com.example.applisondage.model.ProduitModel


class SondageAddProd(val context : MainActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sondage_add_produit, container, false)
        prodList.sortedBy { it.nomProd }
        val buttonReturn = view?.findViewById<Button>(R.id.returnButton6)
        buttonReturn?.setOnClickListener {
            context.loadFragment(SondageAlimentaire(context))
        }
        var filteredProdList = prodList.toMutableList()
        val verticalRV = view?.findViewById<RecyclerView>(R.id.rv_addproduit)
        verticalRV?.adapter = ProduitAdapter(context, prodList, "add", ::updateProgressBar)
        verticalRV?.layoutManager = LinearLayoutManager(context)

        val rechercheProd = view.findViewById<EditText>(R.id.rechercheProd)
        rechercheProd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredProdList.clear()
                for(prod in prodList){
                    if(prod.nomProd.contains(s.toString())){
                        filteredProdList.add(prod)
                    }

                }
                val updatedAdapter = ProduitAdapter(context, filteredProdList, "add", ::updateProgressBar)
                verticalRV?.adapter = updatedAdapter
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })




        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar5)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10




        return view
    }


    private fun updateProgressBar() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar5)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10
    }


    override fun onStart() {
        super.onStart()
        context.onUserLoaded(SondagePresentation(context))
    }

}