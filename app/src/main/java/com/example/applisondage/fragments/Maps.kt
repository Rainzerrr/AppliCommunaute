package com.example.applisondage.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
private val REQUEST_LOCATION_PERMISSION = 1001
class Maps(val context : MainActivity) : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var nom: String = ""
    private lateinit var location: LocationManager
    private var latUtilisateur: Double = 0.0
    private var lonUtilisateur: Double = 0.0
    private lateinit var marqueurUtilisateur: Marker
    private lateinit var marqueurLieu: Marker

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

    fun autorisation() {
        location =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            // Les autorisations sont déjà accordées, vous pouvez demander la localisation
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val newLatLng = LatLng(location.latitude, location.longitude)
            marqueurUtilisateur.position = newLatLng
            mMap.animateCamera(CameraUpdateFactory.newLatLng(marqueurUtilisateur.position))
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
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
        autorisation()

        val markerOptionsUtilisateur = MarkerOptions()
            .position(LatLng(latUtilisateur, latUtilisateur))
            .title("Vous")
            .snippet("Ceci est votre position")
        marqueurUtilisateur = mMap.addMarker(markerOptionsUtilisateur)!!
        mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(latUtilisateur, latUtilisateur)))

        val markerOptions = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(nom)
        marqueurLieu = mMap.addMarker(markerOptions)!!
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(5f)
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