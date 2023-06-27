package com.example.applisondage.fragments

import NetworkTask
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.applisondage.KeepEntries
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.adapter.ProduitAdapter
import com.example.applisondage.model.CurrentUserProvider
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


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
            context.loadFragment(SondageConsultProd(context, ProduitAdapter.produitsChoisis))
        }

        val buttonReturn = view?.findViewById<Button>(R.id.returnButton5)
        buttonReturn?.setOnClickListener {
            context.loadFragment(Enregistrement2(context))
        }

        val buttonFinish = view?.findViewById<Button>(R.id.button_finish)
        buttonFinish?.setOnClickListener {
            if(ProduitAdapter.produitsChoisis.size == 10)
            {
                sendEntriesToServer()
                context.loadFragment(HomeFragment(context))
                KeepEntries.reinitialiserDonnees()
                Toast.makeText(context, "Merci d'avoir répondu au sondage", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, "Veuillez selectionner plus de produits, il vous en manque " + 10.minus(ProduitAdapter.produitsChoisis.size), Toast.LENGTH_SHORT).show()

        }

        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar3)
        progressBar?.progress = 80

        return view
    }
    fun sendEntriesToServer() {
        val baseUrl = "https://privascentreardeche.online/back_end/setSondage.php"

        val nom = URLEncoder.encode(KeepEntries.Entries.nom, "UTF-8")
        val prenom = URLEncoder.encode(KeepEntries.Entries.prenom, "UTF-8")
        val dateNaiss = URLEncoder.encode(KeepEntries.Entries.date, "UTF-8")
        val telephone = URLEncoder.encode(KeepEntries.Entries.telephone, "UTF-8")
        val adresse = URLEncoder.encode(KeepEntries.Entries.adresse, "UTF-8")
        val cp = URLEncoder.encode(KeepEntries.Entries.cp, "UTF-8")
        val ville = URLEncoder.encode(KeepEntries.Entries.ville, "UTF-8")
        val email = URLEncoder.encode(CurrentUserProvider.currentUser?.email, "UTF-8")

        val prodIds = mutableListOf<Int>()
        for (prod in ProduitAdapter.produitsChoisis) {
            prodIds.add(prod.prodId)
        }

        val params = StringBuilder()
        params.append("nom=").append(nom).append("&")
        params.append("prenom=").append(prenom).append("&")
        params.append("datenaissance=").append(dateNaiss).append("&")
        params.append("adresse=").append(adresse).append("&")
        params.append("codepostal=").append(cp).append("&")
        params.append("ville=").append(ville).append("&")
        params.append("tel=").append(telephone).append("&")

        for(i in 1 until prodIds.size+1){
            params.append("alt$i=").append(prodIds[i-1]).append("&")
        }
        val url = URL("$baseUrl?$params")
        val net = NetworkTask()
        net.execute(url.toString())
    }
}


