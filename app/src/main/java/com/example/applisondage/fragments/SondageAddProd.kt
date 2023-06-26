package com.example.applisondage.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.applisondage.R
import com.example.applisondage.adapter.ProduitAdapter
import com.example.applisondage.adapter.SondageAdapter
import com.example.applisondage.model.DecouvModel
import com.example.applisondage.model.ProduitModel

class SondageAddProd(val context : MainActivity) : Fragment() {
    private var prodList: List<ProduitModel> = listOf()
    private var filteredProdList: MutableList<ProduitModel> = mutableListOf()
    private var produitAdapter: ProduitAdapter = ProduitAdapter(context, prodList, "add", ::updateProgressBar)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sondage_add_produit, container, false)

        val buttonReturn = view?.findViewById<Button>(R.id.returnButton6)
        buttonReturn?.setOnClickListener {
            context.loadFragment(SondageAlimentaire(context))
        }


        val rechercheProd = view.findViewById<EditText>(R.id.rechercheProd)
        rechercheProd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })

        val prodList = CreateProdList()

        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar5)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10

        val verticalRV = view?.findViewById<RecyclerView>(R.id.rv_addproduit)
        verticalRV?.adapter = ProduitAdapter(context, prodList, "add", ::updateProgressBar)
        verticalRV?.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun filterProducts(query: String) {
        filteredProdList.clear()
        for (product in prodList) {
            if (product.nomProd.contains(query, true)) {
                filteredProdList.add(product)
            }
        }
        produitAdapter.notifyDataSetChanged()
    }

    private fun updateProgressBar() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar5)
        progressBar?.progress = ProduitAdapter.produitsChoisis.size * 10
    }

    private fun CreateProdList(): List<ProduitModel> {
        // Create and return a dummy product list here
        // Replace with your actual product list
        // This is just a sample
        return listOf(ProduitModel(
            1,
            "sltfdsgfdgf gfdagfd gvfdsvbgdf ssssssssssssssssssssssssss ssss"
        ),
            ProduitModel(
                2,
                "gjkjkj gfdagfd gvfdsvbgdfsssssssssssssssssssssssssssssssssssssssssssssssssss"
            ),

            ProduitModel(
                3,
                "sltfds"
            ),
            ProduitModel(
                4,
                "sltfvfdsvbgdf"
            ),
            ProduitModel(
                5,
                " gvfdsvbgdf"
            ),
            ProduitModel(
                6,
                "sltfdsgljkhgvbgdf"
            ),
            ProduitModel(
                7,
                "sltfdsgfd gvfdsvbgdf"
            ),
            ProduitModel(
                8,
                "sltfdsgfd"
            ),
            ProduitModel(
                9,
                "slhgfdhgf, bfdsvbgdf"
            ),
            ProduitModel(
                10,
                "sltfdsdf"
            ),
            ProduitModel(
                11,
                "sltfdsgfdgf gf;ihgjjkhgbgdf"
            ),
            ProduitModel(
                12,
                "sltfdsgffdagfd gvfdsvbgdf"
            ),
            ProduitModel(
                13,
                "slagfd gvfdsvbgdf"
            )
        )
    }
    override fun onStart() {
        super.onStart()
        context.onUserLoaded(SondagePresentation(context))
    }

}