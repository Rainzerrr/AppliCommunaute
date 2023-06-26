package com.example.applisondage.model

import com.google.firebase.firestore.auth.User

class UserModel (
    val userId:String = "",
    val nom:String = "",
    val prenom: String = "",
    val ville: String = "",
    val email: String = ""
)
