package com.ireny.nf_control_mei.ui.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.databinding.FragmentInvoiceListBinding
import com.ireny.nf_control_mei.navigation.InvoiceListNavigation

import com.ireny.nf_control_mei.utils.navDirections
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.random.Random

class InvoiceListFragment : Fragment() {

    private val navigate: InvoiceListNavigation by navDirections()
    private val invoiceViewModel: InvoiceViewModel by viewModel()

    private var _binding: FragmentInvoiceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceListBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            navigate.invoiceListToHome(requireView())
        }

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
        }

        binding.fab.setOnClickListener {
            navigate.invoiceListToInvoiceRegister(view = requireView())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invoiceViewModel.allInvoices.observe(requireActivity()) { data ->
            data?.let {
                binding.list.adapter = InvoiceRecyclerViewAdapter(values = data, click = ::onInvoiceClicked)
            }
        }
    }

    private fun onInvoiceClicked(item: Invoice){
        navigate.invoiceListToInvoiceRegister(view = requireView(), invoiceId = item.invoiceId?:0)
    }
}