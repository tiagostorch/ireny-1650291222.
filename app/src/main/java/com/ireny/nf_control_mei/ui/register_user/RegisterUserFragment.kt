package com.ireny.nf_control_mei.ui.register_user

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.ireny.nf_control_mei.databinding.FragmentRegisterUserBinding
import com.ireny.nf_control_mei.navigation.RegisterUserNavigation
import com.ireny.nf_control_mei.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterUserFragment : Fragment() {

    private val navigate: RegisterUserNavigation by navDirections()

    private val registerUserViewModel: RegisterUserViewModel by viewModel()

    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
        observeFormState()
        observeUser()
    }

    private fun observeFormState(){
        registerUserViewModel.registerUserFormState.observe(requireActivity(), Observer {
            val state = it ?: return@Observer

            binding.loading.hide()

            binding.saveButton.isEnabled = state.isDataValid

            if (state.usernameError != null) {
                binding.username.error = getString(state.usernameError!!)
            }
            if (state.passwordError != null) {
                binding.password.error = getString(state.passwordError!!)
            }
            if (state.passwordRepeatError != null) {
                binding.passwordVerify.error = getString(state.passwordRepeatError!!)
            }
            if (state.nameError != null) {
                binding.name.error = getString(state.nameError!!)
            }
            if (state.companyIdentifyError != null) {
                binding.companyIdentify.error = getString(state.companyIdentifyError!!)
            }
            if (state.companyNameError != null) {
                binding.companyName.error = getString(state.companyNameError!!)
            }
            if (state.phoneError != null) {
                binding.phone.error = getString(state.phoneError!!)
            }
        })
    }

    private fun observeUser(){
        registerUserViewModel.userResult.observe(requireActivity(), Observer {
            val user = it ?: return@Observer

            binding.loading.hide()

            if (user.error != null) {
                requireContext().showFailed(user.error)
            }

            if (user.success != null) {
                requireActivity().setResult(Activity.RESULT_OK)
                navigate.registerUserToHome(requireView())
            }
        })
    }

    private fun registerChange(){
        registerUserViewModel.formDataChanged(
            password = binding.password.text.toString(),
            passwordRepeat = binding.passwordVerify.text.toString(),
            name = binding.name.text.toString(),
            companyIdentify = binding.companyIdentify.text.toString(),
            companyName = binding.companyName.text.toString(),
            phone = binding.phone.text.toString()
        )
    }

    private fun setupForm(){
        binding.username.setText(FirebaseAuth.getInstance().currentUser?.email?:"")
        binding.name.setText(FirebaseAuth.getInstance().currentUser?.displayName?:"")
        binding.phone.setText(FirebaseAuth.getInstance().currentUser?.phoneNumber?:"")

        if(registerUserViewModel.isCredentialsRegister()) {
            binding.password.afterTextChanged { registerChange() }
            binding.passwordVerify.afterTextChanged { registerChange() }
        } else {
            binding.password.visibility = View.GONE
            binding.passwordVerify.visibility = View.GONE
        }

        binding.name.afterTextChanged { registerChange() }
        binding.companyIdentify.maskedText("[00]{.}[000]{.}[000]{/}[0000]{-}[00]") { registerChange() }
        binding.companyName.afterTextChanged { registerChange() }
        binding.phone.afterTextChanged { registerChange() }
        binding.phone.maskedText("{(}[00]{) }[0] [0000]{-}[0000]") { registerChange() }

        binding.saveButton.setOnClickListener {
            binding.loading.show()
            registerUserViewModel.save(
                username = binding.username.text.toString(),
                password = binding.password.text.toString(),
                name = binding.name.text.toString(),
                companyIdentify = binding.companyIdentify.text.toString(),
                companyName = binding.companyName.text.toString(),
                phone = binding.phone.text.toString()
            )
        }
    }
}