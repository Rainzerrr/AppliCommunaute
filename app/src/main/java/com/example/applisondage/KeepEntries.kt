package com.example.applisondage

import com.google.android.gms.maps.model.LatLng
import java.util.Date


class KeepEntries {

    object Entries {
        var nom: String = ""
        var prenom: String = ""
        var date: String = ""
        var telephone: String = ""
        var adresse: String = ""
        var cp: String = ""
        var ville: String = ""
    }

    companion object{
        fun reinitialiserDonnees(){
            Entries.nom = ""
            Entries.prenom = ""
            Entries.date = ""
            Entries.telephone = ""
            Entries.adresse = ""
            Entries.cp = ""
            Entries.ville = ""

        }
    }


}