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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private val REQUEST_LOCATION_PERMISSION = 1001
class CreationCompte(val context : MainActivity) : Fragment() {
    //Localisation du centre de Paris
    val targetLatitude: Double = 48.8564072
    val targetLongitude: Double = 2.342653
    //Taille du rayon de Paris
    val targetRadius: Double = 60000.0

    private lateinit var location: LocationManager
    private var lastLocation: Location? = null
    var isLocationVerified:Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.create_account_layout, container, false)
        val buttonReturn= view?.findViewById<TextView>(R.id.alreadyAcc)
        buttonReturn?.setOnClickListener {
            context.loadFragment(Connexion(context))
        }

        val verifEmplacement = view?.findViewById<Button>(R.id.verifButton)
        verifEmplacement?.setOnClickListener {
            location = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Veuillez autoriser l'accès à la localisation", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                if (!location.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(context, "Veuillez activer la localisation", Toast.LENGTH_SHORT).show()
                } else {
                    requestLocationUpdates()
                    if (!isLocationVerified){
                        Toast.makeText(context, "Vérification de votre emplacement...", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Emplacement vérifié !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val buttonCreate= view?.findViewById<Button>(R.id.createAccountButton)
        buttonCreate?.setOnClickListener {
            val s1  = view.findViewById<EditText>(R.id.nomEdit)?.text.toString()
            val s2  = view.findViewById<EditText>(R.id.prenomEdit)?.text.toString()
            val s3  = view.findViewById<EditText>(R.id.villeEdit)?.text.toString()
            val s4  =  view.findViewById<EditText>(R.id.emailEdit)?.text.toString()
            val s5  = view.findViewById<EditText>(R.id.passwordEdit)?.text.toString()

            if (!isLocationVerified) {
                Toast.makeText(context, "Veuillez vérifier votre emplacement", Toast.LENGTH_SHORT).show()
            } else if( s1 != "" && s2 != "" && s3 != "" && s4 != "" && s5 != "" && isInGoodLocation()){
                registerUser(s1,s2,s3,s4,s5)
            } else if (!isInGoodLocation()){
                Toast.makeText(context, "Vous n'êtes pas de la région, vous ne pouvez pas créer de compte !", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Veuillez remplir les champs correctement", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()


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
            lastLocation = location
            if (!isLocationVerified){
                isLocationVerified = true
                Toast.makeText(context, "Emplacement vérifié !", Toast.LENGTH_SHORT).show()
            }

            val latitude = location.latitude
            val longitude = location.longitude
            // Utilisez les coordonnées de localisation selon vos besoins
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        if(location != null){
//            location.removeUpdates(locationListener)
//        }
//
//    }

    private fun isInGoodLocation(): Boolean {
        val distances: FloatArray = FloatArray(1)
        Location.distanceBetween(lastLocation!!.latitude, lastLocation!!.longitude, targetLatitude, targetLongitude, distances)
        if (distances[0] < targetRadius){
            return true
        }
        return false
    }


    fun registerUser(nom: String, prenom:String, ville: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // user registration
                    val user = firebaseAuth.currentUser
                    // Save in database
                    val userId = user?.uid
                    val userInformation = hashMapOf(
                        "nom" to nom,
                        "prenom" to prenom,
                        "ville" to ville,
                        "email" to email,
                        "userId" to userId
                    )
                    if (userId != null) {
                        firestore.collection("users").document(userId)
                            .set(userInformation)
                            .addOnCompleteListener { userInfoTask ->
                                if (userInfoTask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Votre compte a été créé",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    context.loadFragment(Connexion(context))
                                } else {
                                    // failed
                                    val exception = userInfoTask.exception
                                    Toast.makeText(
                                        context,
                                        "Champs incorrects !",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                    } else {
                        // failed
                        val exception = task.exception
                        Toast.makeText(context, "Problèmes de création de comptes", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            .addOnFailureListener{
                Toast.makeText(context, "Les champs mot de passe ou email sont mals remplis !", Toast.LENGTH_SHORT).show()
            }
    }
}