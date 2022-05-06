package com.ireny.nf_control_mei.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.databinding.FragmentLoginBinding
import com.ireny.nf_control_mei.navigation.LoginNavigation
import com.ireny.nf_control_mei.ui.register_user.RegisterUserViewModel
import com.ireny.nf_control_mei.utils.afterTextChanged
import com.ireny.nf_control_mei.utils.hide
import com.ireny.nf_control_mei.utils.navDirections
import com.ireny.nf_control_mei.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    companion object{
        const val REQ_ONE_TAP = 998877
    }

    private val navigate: LoginNavigation by navDirections()

    private val loginViewModel: LoginViewModel by viewModel()
    private val registerUserViewModel: RegisterUserViewModel by viewModel()

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val callbackManager : CallbackManager = CallbackManager.Factory.create()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCredentialsLogin()
        setupFacebookLogin()
        setupGoogleLogin()
        setupAutomaticSignIn()
        observeStatusAutomaticLogin()
        observeFormState()
        observeLogin()
        observeUser()
        loginViewModel.checkAutomaticLogin()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.loading.show()
        if(requestCode == REQ_ONE_TAP){
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            loginViewModel.handleGoogleToken(credential)
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupAutomaticSignIn(){
        binding.signInAutoSwitch.setOnCheckedChangeListener { _, isChecked ->
            loginViewModel.setAutomaticLogin(isChecked)
        }
    }

    private fun observeStatusAutomaticLogin(){
        loginViewModel.automaticLogin.observe(requireActivity(), Observer {
            val result = it ?: return@Observer
            binding.signInAutoSwitch.isChecked = result
        })
    }

    private fun observeUser(){
        registerUserViewModel.userResult.observe(requireActivity(), Observer {
            val user = it ?: return@Observer
            if (user.success != null) {
                requireActivity().setResult(RESULT_OK)
                navigate.loginToHome(requireView())
            } else {
                navigate.loginToRegisterUser(requireView())
            }
        })
    }

    private fun setupCredentialsLogin(){
        binding.username.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.createUserOrSignIn(
                            binding.username.text.toString(),
                            binding.password.text.toString()
                        )
                }
                false
            }

            binding.loginButton.setOnClickListener {
                binding.loading.show()
                loginViewModel.createUserOrSignIn(binding.username.text.toString(), binding.password.text.toString())
            }
        }
    }

    private fun setupFacebookLogin(){
        binding.facebookLoginButton.setOnClickListener {
            binding.loading.show()
        }
        binding.facebookLoginButton.setPermissions("email", "public_profile")
        binding.facebookLoginButton.registerCallback(
            callbackManager,
            loginViewModel.facebookCallback
        )
    }

    private fun setupGoogleLogin(){
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        binding.googleLoginButton.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        showFailed(R.string.login_failed)
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    showFailed(R.string.login_failed)
                }
        }
    }

    private fun observeLogin(){
        loginViewModel.loginResult.observe(requireActivity(), Observer {
            val loginResult = it ?: return@Observer

            binding.loading.hide()

            if (loginResult.error != null) {
                showFailed(loginResult.error)
            }

            if (loginResult.success != null) {
                registerUserViewModel.getUserByUid()
            }
        })
    }

    private fun observeFormState(){
        loginViewModel.loginFormState.observe(requireActivity(), Observer {
            val loginState = it ?: return@Observer

            binding.loading.hide()

            binding.loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                binding.password.error = getString(loginState.passwordError)
            }
        })
    }

    private fun showFailed(@StringRes errorString: Int) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}
