package com.example.applisondage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.applisondage.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition

class Maps : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var nom: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.maps, container, false)

        mapView = view.findViewById(R.id.map)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    fun initEmplacement(nom: String) {
        this.nom = nom
        if (nom == "Site du Montoulon") {
            latitude = 44.733528
            longitude = 4.590583
        }
        else if (nom == "Couvent des Récollets de Privas") {
            latitude = 44.735824
            longitude = 4.595412
        }
        else if (nom == "Tour Diane de Poitiers") {
            latitude = 44.735712
            longitude = 4.596898
        }
        else if (nom == "Monument commémoratif des mobiles de l'Ardèche") {
            latitude = 44.736221
            longitude = 4.601438
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val emplacement = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(emplacement).title("Site du Montoulon"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(emplacement))


        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(13f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}