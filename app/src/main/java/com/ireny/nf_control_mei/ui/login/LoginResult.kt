package com.ireny.nf_control_mei.ui.login

import com.google.firebase.auth.FirebaseUser

data class LoginResult(
    val success: FirebaseUser? = null,
    val error: Int? = null
)