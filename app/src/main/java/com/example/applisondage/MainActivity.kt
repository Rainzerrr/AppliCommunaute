package com.example.applisondage
import com.example.applisondage.CustomTransformation.CircleCropTransformation
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.applisondage.fragments.Enregistrement1
import com.example.applisondage.fragments.Enregistrement2
import com.example.applisondage.fragments.HomeFragment
import com.example.applisondage.fragments.Profil
import com.example.applisondage.fragments.SondagePresentation
import com.example.applisondage.model.CurrentUserProvider
import com.example.applisondage.model.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        CurrentUserProvider.currentUser?.let { loadUserDataFromFirestore(it.userId) }


        loadFragment(HomeFragment(this))
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if(getCurrentFragment().toString().substring(0,12) =="HomeFragment") {
                        return@setOnItemSelectedListener true
                    }
                    else {
                        loadFragment(HomeFragment(this))
                        return@setOnItemSelectedListener true
                    }
                }
                R.id.sondage -> {
                    loadFragment(SondagePresentation(this))
                    return@setOnItemSelectedListener true
                }
                R.id.person -> {
                    loadFragment(SondagePresentation(this))
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }


    }
    fun getCurrentFragment(): Fragment? {
        val fragmentId = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return fragmentId
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity.currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    fun loadFragment(fragment: Fragment) {
        hideKeyboard(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    fun loadUserDataFromFirestore(userId: String) {
        val userRef = firestore.collection("users").document(userId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserModel::class.java)
                CurrentUserProvider.currentUser = user
                onUserLoaded(HomeFragment(MainActivity()))
            } else {

            }
        }.addOnFailureListener { exception ->

        }
    }

    fun loadImage(userId: String) {
        val storageReference = FirebaseStorage.getInstance().reference.child("profile_photos/$userId.jpg")
        val image = findViewById<ImageView>(R.id.img_profil1)
        val image2 = findViewById<ImageView>(R.id.img_profil2)

        storageReference.downloadUrl.addOnSuccessListener { uri ->

                if(image != null) {
                Glide.with(this)
                    .load(uri)
                    .transform(CircleCropTransformation())
                    .into(image)
            }
                if(image2 != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(CircleCropTransformation())
                        .into(image2)
                }
        }.addOnFailureListener { exception ->

        }
    }

    fun onUserLoaded(fragment: Fragment) {
        if(CurrentUserProvider.currentUser != null) {
            if(fragment.toString().substring(0,12) == HomeFragment(MainActivity()).toString().substring(0,12)) {
                val nomPrenom = findViewById<TextView>(R.id.Nom)
                loadImage(CurrentUserProvider.currentUser!!.userId)

                nomPrenom.text = CurrentUserProvider.currentUser?.nom + " " + CurrentUserProvider.currentUser?.prenom
            }
            else if(fragment.toString().substring(0,6) == Profil(MainActivity()).toString().substring(0,6)){
                val nomPrenom = findViewById<TextView>(R.id.nameTextView)
                val email = findViewById<TextView>(R.id.emailTextView)
                val ville = findViewById<TextView>(R.id.villeTextView)
                loadImage(CurrentUserProvider.currentUser!!.userId)
                nomPrenom.text = CurrentUserProvider.currentUser?.nom + " " + CurrentUserProvider.currentUser?.prenom
                email.text = CurrentUserProvider.currentUser?.email
                ville.text = CurrentUserProvider.currentUser?.ville
            }
            else if(fragment.toString().substring(0,15) == Enregistrement1(MainActivity()).toString().substring(0,15)){
                val nom = findViewById<EditText>(R.id.enregistrementNom)
                val prenom = findViewById<EditText>(R.id.enregistrementPrenom)
                nom.setText(CurrentUserProvider.currentUser?.nom)
                prenom.setText(CurrentUserProvider.currentUser?.prenom)
            }
            else if(fragment.toString().substring(0,15) == Enregistrement2(MainActivity()).toString().substring(0,15)){
                val ville = findViewById<EditText>(R.id.enregistrementVille)
                ville.setText(CurrentUserProvider.currentUser?.ville)

            }
        }

    }
}