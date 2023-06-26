package com.example.applisondage.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.applisondage.model.ActuModel
import com.example.applisondage.model.DecouvModel
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.adapter.DecouvAdapter
import com.example.applisondage.adapter.ActuAdapter
import com.example.applisondage.model.CurrentUserProvider
import com.example.applisondage.model.UserModel

class HomeFragment(val context : MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?) : View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val profil = view?.findViewById<ImageView>(R.id.img_profil1)
        profil?.setOnClickListener{
            if(CurrentUserProvider.currentUser ==null)
                context.loadFragment(Connexion(context))
            else
                context.loadFragment(Profil(context))
        }
        val actuList = arrayListOf<ActuModel>()
        actuList.add(
            ActuModel(
            "Les horaires changent",
            "transport"
        )
        )
        actuList.add(
            ActuModel(
                "Inauguration théatrale",
                "the"
            )
        )

        actuList.add(
            ActuModel(
                "Appel à projet",
                "ap"
            )
        )

        actuList.add(
            ActuModel(
                "Réunion importante",
                "reunion"
            )
        )

        val decouvList = arrayListOf<DecouvModel>()

        decouvList.add(
            DecouvModel(
                "Site du Montoulon",
                "https://cdn-s-www.ledauphine.com/images/737B9792-D825-48D4-AAFA-4373BA5A16D7/NW_raw/elles-sont-visibles-de-jour-comme-de-nuit-depuis-le-centre-ville-de-privas-les-troix-croix-du-montoulon-font-parties-des-symboles-de-la-capitale-ardechoise-elles-annoncent-aussi-l-arrivee-au-sommet-du-site-historique-photo-le-dl-anthony-gonzalez-1631978299.jpg"
            )
        )
        decouvList.add(
            DecouvModel(
                "Couvent des Récollets de Privas",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Chapelle_du_couvent_des_r%C3%A9collets_Cambrai.JPG/1200px-Chapelle_du_couvent_des_r%C3%A9collets_Cambrai.JPG"
            )
        )
        decouvList.add(
            DecouvModel(
                "Tour Diane de Poitiers",
"https://media-cdn.tripadvisor.com/media/photo-s/08/6f/9d/d8/office-de-tourisme-privas.jpg"            )
        )
        decouvList.add(
                DecouvModel(
                    "Monument commémoratif des mobiles de l'Ardèche",
                "https://upload.wikimedia.org/wikipedia/commons/1/1d/Mobiles%CC%A0_ardeche_vernon_20220610_11.jpg"
                )
                )

        val horizontalRV1 = view?.findViewById<RecyclerView>(R.id.rv_actu)
        horizontalRV1?.adapter = ActuAdapter(context, actuList)


        val horizontalRV2 = view?.findViewById<RecyclerView>(R.id.rv_decouv)
        horizontalRV2?.adapter = DecouvAdapter(context, decouvList)
        return view
    }

    override fun onStart() {
        super.onStart()
        context.onUserLoaded(HomeFragment(MainActivity()))
    }

}