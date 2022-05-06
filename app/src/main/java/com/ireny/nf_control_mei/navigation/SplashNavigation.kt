package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.splash.SplashFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class SplashNavigation {
    fun splashToLogin(view: View) {
        view.navigateTo(
            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }

    fun splashToHome(view: View) {
        view.navigateTo(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        )
    }

    fun splashToRegisterUser(view: View) {
        view.navigateTo(
            SplashFragmentDirections.actionSplashFragmentToRegisterUserFragment()
        )
    }
}