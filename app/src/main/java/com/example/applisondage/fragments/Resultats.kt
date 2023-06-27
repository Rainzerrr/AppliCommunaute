package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R


class Resultats(val context : MainActivity) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.resultats_layout, container, false)
        val chartWebView = view.findViewById<WebView>(R.id.chartWebView)
        chartWebView.settings.javaScriptEnabled = true
        chartWebView.loadUrl("https://privascentreardeche.online/layouts/resultatSondage.php")


        return view
    }
}