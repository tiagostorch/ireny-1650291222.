package com.ireny.nf_control_mei.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.databinding.FragmentHomeBinding
import com.ireny.nf_control_mei.navigation.HomeNavigation
import com.ireny.nf_control_mei.ui.base.BaseViewModel
import com.ireny.nf_control_mei.ui.login.LoginViewModel
import com.ireny.nf_control_mei.utils.navDirections
import com.ireny.nf_control_mei.utils.showFailed
import com.ireny.nf_control_mei.utils.showOrHide
import com.ireny.nf_control_mei.utils.toRealFormat
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val navigate: HomeNavigation by navDirections()

    private val loginViewModel: LoginViewModel by viewModel()
    private val baseViewModel: BaseViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.username.text = loginViewModel.getDisplayUserName()

        baseViewModel.mockData()

        binding.logoutButton.setOnClickListener {
            loginViewModel.signOut()
            navigate.homeToSplash(requireView())
        }

        binding.menu.setOnClickListener {
            binding.menuContent.showOrHide()
        }

        binding.menuItemInvoiceHistory.setOnClickListener {
            navigate.homeToInvoiceList(requireView())
        }

        binding.menuItemSettings.setOnClickListener {
            navigate.homeToPreferences(requireView())
        }

        binding.fabNewInvoice.setOnClickListener {
            navigate.homeToInvoiceRegister(requireView())
        }

        observeBillingData()
        baseViewModel.getBillingData()
    }

    private fun observeBillingData(){
        baseViewModel.billingResult.observe(requireActivity(), Observer {
            val billing = it ?: return@Observer

            if (billing.error != null) {
                requireContext().showFailed(billing.error)
            }

            if (billing.success != null) {
                binding.progressBarCharTitle.text = "Faturamento ${billing.success.year}"
                binding.progressBarChar1Value.text = billing.success.value.toRealFormat()
                binding.progressBarChar1Total.text = billing.success.limit.toRealFormat()
                binding.progressBarChar1Bottom.text = "Restam ${(billing.success.limit - billing.success.value).toRealFormat()}"

                binding.progressBarChar1.max = billing.success.limit.toInt()
                binding.progressBarChar1.progress = billing.success.value.toInt()
            }
        })
    }


    private  fun setupChart(){
        val chart = binding.progressBarChar1

    }
}