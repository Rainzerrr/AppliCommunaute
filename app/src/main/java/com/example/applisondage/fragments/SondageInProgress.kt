package com.example.applisondage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.applisondage.MainActivity


class SondageInProgress(val context : MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater.inflate(com.example.applisondage.R.layout.fragment_sondage_in_progress, container, false)
        val buttonReturn= view?.findViewById<Button>(com.example.applisondage.R.id.returnButton3)
        buttonReturn?.setOnClickListener{
            context.loadFragment(SondagePresentation(context))
        }
        return view

    }
    override fun onStart() {
        super.onStart()
        context.onUserLoaded(SondageInProgress(context))
    }
}