package com.ireny.nf_control_mei.ui.splash

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.navigation.SplashNavigation
import com.ireny.nf_control_mei.ui.login.LoginViewModel
import com.ireny.nf_control_mei.ui.register_user.RegisterUserViewModel
import com.ireny.nf_control_mei.utils.navDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    private val navigate: SplashNavigation by navDirections()

    private val loginViewModel: LoginViewModel by viewModel()
    private val registerUserViewModel: RegisterUserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAutomaticLogin()
        loginViewModel.checkAutomaticLogin()
        observeUser()
    }

    private fun observeUser(){
        registerUserViewModel.userResult.observe(requireActivity(), Observer {
            val userLocal = it ?: return@Observer
            val userLogged = loginViewModel.userLogged()
            if(userLogged){
                if (userLocal.success != null) {
                    requireActivity().setResult(Activity.RESULT_OK)
                    navigate.splashToHome(requireView())
                } else {
                    navigate.splashToRegisterUser(requireView())
                }
            } else {
                navigate.splashToLogin(requireView())
            }
        })
    }

    private fun observeAutomaticLogin(){
        loginViewModel.automaticLogin.observe(requireActivity(), Observer {
            val result = it ?: return@Observer
            val userLogged = loginViewModel.userLogged()
            if (result) {
                registerUserViewModel.getUserByUid()
            } else {
                if(userLogged) {
                    loginViewModel.signOut()
                }
                navigate.splashToLogin(requireView())
            }
        })
    }
}