package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.MainActivity
import com.example.applisondage.adapter.SondageAdapter
import com.example.applisondage.model.SondageModel


class SondagePresentation(val context : MainActivity) : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater.inflate(com.example.applisondage.R.layout.activity_sondage_presentation, container, false)
        val sondageList = arrayListOf<SondageModel>()
        val sondHome = SondageHome(context)
        val sondInProg = SondageInProgress(context)
        sondageList.add(
            SondageModel(
                "Sujet : Habitudes alimentaires",
                "Durée : 5 minutes",
                sondHome
            )
        )
        sondageList.add(
            SondageModel(
                "Sujet : Habitudes de transports",
                "Durée : 3 minutes",
                sondInProg
            )
        )
        sondageList.add(
            SondageModel(
                "Sujet : Monument préféré",
                "Durée : 2 minutes",
                sondInProg
            )
        )

        sondageList.add(
            SondageModel(
                "Sujet : Avis sur l'application",
                "Durée : 7 minutes",
                sondInProg
            )
        )
        sondageList.add(
            SondageModel(
                "Sujet : Avis sur les nouveautés de la commune",
                "Durée : 5 minutes",
                sondInProg
            )
        )
        val verticalRV = view?.findViewById<RecyclerView>(com.example.applisondage.R.id.rv_sondages)
        verticalRV?.adapter = SondageAdapter(context, sondageList)
        verticalRV?.layoutManager = LinearLayoutManager(context)
        return view
    }
    override fun onStart() {
        super.onStart()
        context.onUserLoaded(SondagePresentation(context))
    }
}