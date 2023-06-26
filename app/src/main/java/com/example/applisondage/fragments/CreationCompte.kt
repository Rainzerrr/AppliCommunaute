package com.example.applisondage.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log.e
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
    private lateinit var location: LocationManager
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
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                // Les autorisations sont déjà accordées, vous pouvez demander la localisation
                requestLocationUpdates()
            }
        }

        val buttonCreate= view?.findViewById<Button>(R.id.createAccountButton)
        buttonCreate?.setOnClickListener {
            val s1  = view.findViewById<EditText>(R.id.nomEdit)?.text.toString()
            val s2  = view.findViewById<EditText>(R.id.prenomEdit)?.text.toString()
            val s3  = view.findViewById<EditText>(R.id.villeEdit)?.text.toString()
            val s4  =  view.findViewById<EditText>(R.id.emailEdit)?.text.toString()
            val s5  = view.findViewById<EditText>(R.id.passwordEdit)?.text.toString()
            if( s1 != "" && s2 != "" && s3 != "" && s4 != "" && s5 != "" && true){
                registerUser(s1,s2,s3,s4,s5,true)
            }
            else if (false){
                Toast.makeText(context, "Vous n'êtes pas de la région, vous ne pouvez pas créer de compte !", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, "Veuillez remplir les champs correctement", Toast.LENGTH_SHORT).show()

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
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

    fun registerUser(nom: String, prenom:String, ville: String, email: String, password: String, verifyLocation: Boolean) {
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