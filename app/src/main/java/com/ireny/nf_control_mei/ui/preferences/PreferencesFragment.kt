package com.ireny.nf_control_mei.ui.preferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.databinding.FragmentPreferencesBinding
import com.ireny.nf_control_mei.navigation.PreferencesNavigation
import com.ireny.nf_control_mei.ui.base.BaseViewModel
import com.ireny.nf_control_mei.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreferencesFragment : Fragment() {

    private val navigate: PreferencesNavigation by navDirections()
    private val baseViewModel: BaseViewModel by viewModel()

    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.back.setOnClickListener {
            navigate.preferencesToHome(requireView())
        }

        binding.meiLimitValue.currencyText()

        binding.limitMeiSaveButton.setOnClickListener {
            baseViewModel.saveLimitMei(binding.meiLimitValue.text.toString())
        }

        observeLimitMei()
        baseViewModel.getLimitMei()
        observeSaved()
    }

    private fun observeLimitMei(){
        baseViewModel.limitMei.observe(requireActivity(), Observer {
            val result = it ?: return@Observer
            binding.meiLimitValue.setText(result.toRealFormat())
        })
    }

    private fun observeSaved(){
        baseViewModel.saveResult.observe(requireActivity(), Observer {
            val saved = it ?: return@Observer
            if (saved.error != null) {
                binding.meiLimitValue.error = getString(saved.error)
                requireContext().showFailed(saved.error)
            }

            if (saved.success != null) {
                requireContext().showFailed(
                    R.string.success_process
                )
            }
        })
    }
}