package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.login.LoginFragmentDirections
import com.ireny.nf_control_mei.ui.splash.SplashFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class LoginNavigation {

    fun loginToHome(view: View) {
        view.navigateTo(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        )
    }

    fun loginToRegisterUser(view: View) {
        view.navigateTo(
            LoginFragmentDirections.actionLoginFragmentToRegisterUserFragment()
        )
    }
}