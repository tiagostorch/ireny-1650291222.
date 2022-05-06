package com.ireny.nf_control_mei.navigation


import android.view.View
import com.ireny.nf_control_mei.ui.register_user.RegisterUserFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class RegisterUserNavigation {

    fun registerUserToHome(view: View) {
        view.navigateTo(
            RegisterUserFragmentDirections.actionRegisterUserFragmentToHomeFragment()
        )
    }
}