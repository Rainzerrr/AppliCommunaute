package com.example.applisondage.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity


class Enregistrement2(val context : MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        val view = inflater?.inflate(com.example.applisondage.R.layout.activity_enregistrement2, container, false)
        val buttonReturn= view?.findViewById<Button>(com.example.applisondage.R.id.returnButton2)
        buttonReturn?.setOnClickListener(View.OnClickListener {
            context.loadFragment(Enregistrement1(context))
        })
        val buttonStart= view?.findViewById<Button>(com.example.applisondage.R.id.button_next_2)
        buttonStart?.setOnClickListener(View.OnClickListener {
            context.loadFragment(Fragment())//FAIRE LE FRAGMENT ALED -------------------------------------------
            //--------------------------------------------------------------
            //--------------------------------------------------------------
            //--------------------------------------------------------------
        })
        return view
    }
    override fun onStart() {
        super.onStart()
        context.onUserLoaded(Enregistrement2(context))
    }
}