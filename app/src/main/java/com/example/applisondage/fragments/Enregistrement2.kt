package com.example.applisondage.fragments

import android.R
import android.os.Bundle
import android.support.annotation.Keep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.applisondage.KeepEntries
import com.example.applisondage.MainActivity


class Enregistrement2(val context : MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater.inflate(com.example.applisondage.R.layout.activity_enregistrement2, container, false)
        val buttonReturn= view?.findViewById<Button>(com.example.applisondage.R.id.returnButton2)
        buttonReturn?.setOnClickListener {
            updateInfos()
            context.loadFragment(Enregistrement1(context))
        }
        val buttonNext= view?.findViewById<Button>(com.example.applisondage.R.id.button_next_2)
        buttonNext?.setOnClickListener {
            updateInfos()
            if(KeepEntries.Entries.adresse=="" || KeepEntries.Entries.cp=="" || KeepEntries.Entries.ville=="" )
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            else {
                context.loadFragment(SondageAlimentaire(context))
            }
        }

        val progressBar = view?.findViewById<ProgressBar>(com.example.applisondage.R.id.progressBar2)
        progressBar?.progress = 50
        return view
    }
    override fun onStart() {
        super.onStart()
        view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementAdress)?.setText( KeepEntries.Entries.adresse)
        view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementCp)?.setText( KeepEntries.Entries.cp)
        view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementVille)?.setText( KeepEntries.Entries.ville)
        context.onUserLoaded(Enregistrement2(context))
    }

    fun updateInfos(){
        KeepEntries.Entries.adresse = view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementAdress)?.text.toString()
        KeepEntries.Entries.cp = view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementCp)?.text.toString()
        KeepEntries.Entries.ville = view?.findViewById<EditText>(com.example.applisondage.R.id.enregistrementVille)?.text.toString()
    }
}