package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R


class SondageHome(val context : MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_sondage_home, container, false)
        val buttonStart = view?.findViewById<Button>(R.id.button_start)
        buttonStart?.setOnClickListener{
            context.loadFragment(Enregistrement1(context))
        }

        val buttonResult = view?.findViewById<Button>(R.id.button_results)
        buttonResult?.setOnClickListener{
            context.loadFragment(Resultats(context))
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        context.onUserLoaded(SondageHome(context))
    }
}