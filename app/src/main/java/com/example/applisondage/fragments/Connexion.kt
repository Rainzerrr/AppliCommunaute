package com.example.applisondage.fragments


import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.applisondage.MainActivity
import com.example.applisondage.R
import com.example.applisondage.model.CurrentUserProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class Connexion(val context : MainActivity) : Fragment() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.connexion_layout, container, false)
        val buttonReturn= view?.findViewById<TextView>(R.id.noaccount)
        buttonReturn?.setOnClickListener {
            context.loadFragment(CreationCompte(context))
        }
        val buttonConnect= view?.findViewById<TextView>(R.id.loginButton)
        buttonConnect?.setOnClickListener {
            signIn(view,view.findViewById<EditText>(R.id.emailConnexion)?.text.toString() ,view?.findViewById<EditText>(R.id.passwordConnexion)?.text.toString() )
        }
        val buttonForgotPassword = view?.findViewById<TextView>(R.id.forgotPasswordTextView)
        buttonForgotPassword?.setOnClickListener {
            val email =  view.findViewById<TextView>(R.id.emailConnexion).text.toString()
            if(email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {

                        Toast.makeText(context, "Email envoyé", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        // Failed to send password reset email
                        Toast.makeText(
                            context,
                            "Il n'existe aucun utilisateur avec cette adresse mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(context, "Entrez votre adresse email", Toast.LENGTH_SHORT).show()
            }
            }

        return view
    }




    // Authenticate with custom token
    fun authenticateUserWithCustomToken(customToken: String) {
        firebaseAuth.signInWithCustomToken(customToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // authentication successful
                    val user = firebaseAuth.currentUser
                    context.loadFragment(HomeFragment(context))
                } else {
                    // authentication failed
                    val exception = task.exception
                }
            }
    }

    fun signIn(view: View, username: String, password: String) {
        if(username != "" && password!="") {
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User signed in successfully
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            val users = context.loadUserDataFromFirestore(user.uid)

                            CurrentUserProvider.currentUser?.let { users }
                            context.loadFragment(HomeFragment(context))
                        }

                    } else {
                        // User sign in failed
                        val exception = task.exception
                        if (exception != null) {
                            showErrorMessage(view, "Identifiant ou mot de passe incorrect !")
                        }
                    }
                }
        }
        else showErrorMessage(view, "Veuillez remplir les champs !")
    }




    // Sign out
    companion object{
        private val firebaseAuth = FirebaseAuth.getInstance()
        fun signOut() {
            firebaseAuth.signOut()
            CurrentUserProvider.currentUser = null
        }
        fun showErrorMessage(view: View, message: String) {
            val errorTextView = view.findViewById<TextView>(R.id.errorTextView)
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
        }
    }




}
