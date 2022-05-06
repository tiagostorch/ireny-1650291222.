package com.ireny.nf_control_mei.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.data.LocalDataStore
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(private val localDataStore: LocalDataStore): ViewModel(),FacebookCallback<com.facebook.login.LoginResult>,
    OnCompleteListener<AuthResult>, OnFailureListener  {

    val facebookCallback : FacebookCallback<com.facebook.login.LoginResult> = this

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _automaticLogin = MutableLiveData<Boolean>()
    val automaticLogin: LiveData<Boolean> = _automaticLogin

    fun checkAutomaticLogin() {
        viewModelScope.launch {
            _automaticLogin.value = localDataStore.getAutomaticLogin()
        }
    }

    fun setAutomaticLogin(automatic:Boolean){
        viewModelScope.launch {
            localDataStore.setAutomaticLogin(automatic)
        }
    }

    fun getDisplayUserName():String {
        FirebaseAuth.getInstance().currentUser?.run {
            if(!this.displayName.isNullOrBlank()){
                return this.displayName!!
            }
            return this.email!!
        }
        return ""
    }

    fun userLogged():Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun createUserOrSignIn(username: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username,password)
            .addOnCompleteListener(this)
            .addOnFailureListener {
                if(it is FirebaseAuthUserCollisionException){
                    login(username,password)
                }else {
                    _loginResult.value =
                        LoginResult(success = null, error = R.string.login_failed)
                }
            }
    }

    private fun login(username: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword (username,password)
            .addOnCompleteListener(this)
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthInvalidUserException,
                    is FirebaseAuthEmailException -> {
                        _loginForm.value = LoginFormState(usernameError = R.string.login_failed_invalid_username)
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        _loginForm.value = LoginFormState(passwordError = R.string.login_failed_invalid_password)
                    }
                    else -> {
                        _loginResult.value =
                            LoginResult(success = null, error = R.string.login_failed)
                    }
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this)
            .addOnFailureListener(this)
    }

    fun handleGoogleToken(signInCredential: SignInCredential) {
        if(signInCredential.googleIdToken != null) {
            val credential = GoogleAuthProvider.getCredential(signInCredential.googleIdToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this)
                .addOnFailureListener(this)
        } else {
            _loginResult.value =
                LoginResult(success = null, error = R.string.login_failed)
        }
    }

    override fun onSuccess(result: com.facebook.login.LoginResult) {
        handleFacebookAccessToken(result.accessToken)
    }

    override fun onCancel() {
        _loginResult.value =
            LoginResult(success = null, error = R.string.login_failed)
    }

    override fun onError(error: FacebookException) {
        _loginResult.value =
            LoginResult(success = null, error = R.string.login_failed)
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            _loginResult.value =
                LoginResult(success = FirebaseAuth.getInstance().currentUser)
        }
    }

    override fun onFailure(fail: Exception) {
        _loginResult.value =
            LoginResult(success = null, error = R.string.login_failed)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        AccessToken.setCurrentAccessToken(null)
    }
}