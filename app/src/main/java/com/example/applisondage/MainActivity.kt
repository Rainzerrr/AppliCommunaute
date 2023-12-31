package com.example.applisondage
import ApiService
import com.example.applisondage.CustomTransformation.CircleCropTransformation
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.applisondage.fragments.Enregistrement1
import com.example.applisondage.fragments.Enregistrement2
import com.example.applisondage.fragments.HomeFragment
import com.example.applisondage.fragments.Profil
import com.example.applisondage.fragments.SondagePresentation
import com.example.applisondage.model.CurrentUserProvider
import com.example.applisondage.model.ProduitModel
import com.example.applisondage.model.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)


        val apiService = ApiService(this, "https://privascentreardeche.online/back_end/getResultat.php")
        apiService.fetchData(
            onSuccess = { response ->
                // Handle the JSON response here
                processResponse(response)
            },
            onError = { error ->

            }
        )

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

    private fun processResponse(response: JSONArray) {
        // Parse and process the JSON response
        for (i in 0 until response.length()) {
            val item: JSONObject = response.getJSONObject(i)
            val produit = ProduitModel(parseInt(item["alim_code"].toString()),item["alim_nom_fr"].toString())
            prodList.add(produit)
        }
    }

    fun getCurrentFragment(): Fragment? {
        val fragmentId = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return fragmentId
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity.currentFocus
        currentFocusView?.let { view ->
            val imm = ContextCompat.getSystemService(activity, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
        //currentFocusView?.clearFocus()
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
                nomPrenom.text = CurrentUserProvider.currentUser?.prenom + " " + CurrentUserProvider.currentUser?.nom
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

    companion object{
        val prodList :MutableList<ProduitModel> = mutableListOf()
    }
    }
