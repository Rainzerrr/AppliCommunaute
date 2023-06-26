package com.example.applisondage.fragments

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.example.applisondage.R
import java.util.Calendar

class Enregistrement1(val context : MainActivity) : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_enregistrement1, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar1)
        progressBar?.progress = 15
        val buttonNext = view?.findViewById<Button>(R.id.button_next)

        val naiss = view.findViewById<EditText>(R.id.enregistrementNaiss)
        buttonNext?.setOnClickListener {
            updateInfos()
            if(KeepEntries.Entries.nom=="" || KeepEntries.Entries.prenom=="" || KeepEntries.Entries.date=="" || KeepEntries.Entries.telephone=="")
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            else{
                context.loadFragment(Enregistrement2(context))
            }


        }


        val buttonReturn = view?.findViewById<Button>(R.id.returnButton)
        buttonReturn?.setOnClickListener {
            updateInfos()
            context.loadFragment(SondageHome(context))
        }

        naiss?.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    naiss.setText(formattedDate)
                }, 2000, 0, 1)

            datePickerDialog.show()


        }

        return view
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<EditText>(R.id.enregistrementNom)?.setText( KeepEntries.Entries.nom)
        view?.findViewById<EditText>(R.id.enregistrementPrenom)?.setText( KeepEntries.Entries.prenom)
        view?.findViewById<EditText>(R.id.enregistrementNaiss)?.setText( KeepEntries.Entries.date)
        view?.findViewById<EditText>(R.id.enregistrementTel)?.setText( KeepEntries.Entries.telephone)

        context.onUserLoaded(Enregistrement1(context))
    }

    fun updateInfos(){
        KeepEntries.Entries.nom = view?.findViewById<EditText>(R.id.enregistrementNom)?.text.toString()
        KeepEntries.Entries.prenom = view?.findViewById<EditText>(R.id.enregistrementPrenom)?.text.toString()
        KeepEntries.Entries.date = view?.findViewById<EditText>(R.id.enregistrementNaiss)?.text.toString()
        KeepEntries.Entries.telephone = view?.findViewById<EditText>(R.id.enregistrementTel)?.text.toString()
    }
}