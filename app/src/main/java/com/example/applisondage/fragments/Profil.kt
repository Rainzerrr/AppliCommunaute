package com.example.applisondage.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Profil(val context: MainActivity) : Fragment() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profil_layout, container, false)
        val buttonReturn = view.findViewById<Button>(R.id.returnButton4)
        buttonReturn?.setOnClickListener {
            context.loadFragment(HomeFragment(context))
        }
        val buttonLogout = view.findViewById<Button>(R.id.logoutButton)
        buttonLogout?.setOnClickListener {
            Connexion.signOut()
            context.loadFragment(Connexion(context))
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                loadImage(data)
            }
        }
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted, launch the gallery or file picker
                launchGallery()
            } else {
                // Permission is not granted, show a message or handle it accordingly
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        val changePhotoButton = view.findViewById<Button>(R.id.changePhotoButton)
        changePhotoButton.setOnClickListener {
            permissionLauncher.launch(permission)
        }



        return view
    }


    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launcher.launch(intent)
    }



    private fun loadImage(data: Intent?) {

        val db = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val selectedImageUri: Uri? = data?.data
        if (selectedImageUri != null) {

            val storageRef = FirebaseStorage.getInstance().reference
            val profilePhotoRef =
                storageRef.child("profile_photos/${currentUser?.uid}.jpg")

            profilePhotoRef.putFile(selectedImageUri)
                .continueWithTask { uploadTask ->
                    if (!uploadTask.isSuccessful) {
                        uploadTask.exception?.let { throw it }
                    }
                    profilePhotoRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()


                        currentUser?.uid?.let { userId ->
                            val userRef = db.collection("users").document(userId)
                            userRef.update("profilePhotoUrl", downloadUrl)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Photo de profil changée !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    context.loadFragment(HomeFragment(context))
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "L'image choisie n'est pas conforme: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        // Error occurred while uploading the image
                        Toast.makeText(
                            context,
                            "L'image choisie n'est pas conforme: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }




    override fun onStart() {
        super.onStart()
        context.onUserLoaded(Profil(context))
    }
}