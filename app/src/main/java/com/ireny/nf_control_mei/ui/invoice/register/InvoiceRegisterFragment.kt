package com.ireny.nf_control_mei.ui.invoice.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.databinding.FragmentInvoiceRegisterBinding
import com.ireny.nf_control_mei.navigation.InvoiceRegisterNavigation
import com.ireny.nf_control_mei.ui.base.BaseViewModel
import com.ireny.nf_control_mei.ui.base.adapters.CompanyAdapter
import com.ireny.nf_control_mei.ui.invoice.InvoiceViewModel
import com.ireny.nf_control_mei.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InvoiceRegisterFragment : Fragment() {

    private val navigate: InvoiceRegisterNavigation by navDirections()
    private val arguments by navArgs<InvoiceRegisterFragmentArgs>()

    private val baseViewModel: BaseViewModel by viewModel()
    private val invoiceViewModel: InvoiceViewModel by viewModel()

    private var _binding: FragmentInvoiceRegisterBinding? = null
    private val binding get() = _binding!!

    private val companyAdapter: CompanyAdapter = CompanyAdapter(click = ::onCompanySelected)

    private var company: Company? = null
    private var invoice: Invoice? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceRegisterBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            navigate.invoiceRegisterToInvoiceList(requireView())
        }

        with(binding.companiesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = companyAdapter
        }

        binding.search.afterTextChanged { executeSearch() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseViewModel.allCompanies.observe(requireActivity()) { data ->
            data?.let {
                companyAdapter.updateList(data)
            }
        }

        setupForm()
        observeInvoice()
        observeFormState()
        observeSaved()

        if(arguments.invoiceId > 0){
            invoiceViewModel.getInvoice(arguments.invoiceId)
        }
    }

    private fun observeInvoice() {
        invoiceViewModel.invoiceResult.observe(requireActivity(), Observer {
            val invoice = it ?: return@Observer
            binding.loading.hide()

            if (invoice.error != null) {
                requireContext().showFailed(invoice.error)
            }

            if (invoice.success != null) {
                loadView(invoice.success)
            }
        })
    }

    private fun loadView(invoice: Invoice) {
        this.invoice = invoice
        this.company = invoice.company
        binding.value.setText(invoice.invoiceValue.toRealFormat())
        binding.number.setText(invoice.invoiceNumber)
        binding.month.setText(invoice.month)
        binding.received.setText(invoice.receivedAt.toDateTextFormatted())
        binding.description.setText(invoice.invoiceDescription)
        showForm()
        binding.removeButton.show()
    }

    private fun onCompanySelected(item: Company){
        company = item
        binding.search.setText("")
        showForm()
    }

    private fun executeSearch(){
        val s = binding.search.text.toString()
        if(s.isEmpty() && company != null){
            showForm()
        } else {
            hideForm()
            companyAdapter.filter(s)
        }
    }

    private fun hideForm(){
        binding.companiesList.show()
        binding.companySelected.hide()
        binding.formContent.hide()
        binding.buttonsForm.hide()
    }

    private fun showForm(){
        binding.companiesList.hide()
        binding.companySelected.text = "${company?.companyIdentifier} ${company?.companyName}"
        binding.companySelected.show()
        binding.formContent.show()
        binding.buttonsForm.show()
    }

    private fun setupForm(){
        binding.value.currencyText { registerChange() }
        binding.number.afterTextChanged { registerChange() }
        binding.month.maskedText("[00]") { registerChange() }
        binding.received.maskedText("[00]{/}[00]{/}[0000]") { registerChange() }
        binding.description.afterTextChanged { registerChange() }

        binding.saveButton.setOnClickListener {
            binding.loading.show()
            invoiceViewModel.save(
                value = binding.value.text.toString(),
                number = binding.number.text.toString(),
                month = binding.month.text.toString(),
                received = binding.received.text.toString(),
                description = binding.description.text.toString(),
                company = company!!,
                invoice = invoice
            )
        }

        binding.removeButton.setOnClickListener {
            requireContext().openDialog(getString(R.string.delete_title),getString(R.string.delete_message),true,null) {
                invoice?.run {
                    invoiceViewModel.delete(this)
                }
            }
        }
    }

    private fun registerChange(){
        invoiceViewModel.formDataChanged(
            value = binding.value.text.toString(),
            number = binding.number.text.toString(),
            month = binding.month.text.toString(),
            received = binding.received.text.toString(),
            description = binding.description.text.toString()
        )
    }

    private fun observeFormState(){
        invoiceViewModel.registerInvoiceFormState.observe(requireActivity(), Observer {
            val state = it ?: return@Observer

            binding.loading.visibility = View.GONE
            binding.saveButton.isEnabled = state.isDataValid

            if (state.valueError != null) {
                binding.value.error = getString(state.valueError!!)
            }
            if (state.numberError != null) {
                binding.number.error = getString(state.numberError!!)
            }
            if (state.monthError != null) {
                binding.month.error = getString(state.monthError!!)
            }
            if (state.receivedError != null) {
                binding.received.error = getString(state.receivedError!!)
            }
            if (state.descriptionError != null) {
                binding.description.error = getString(state.descriptionError!!)
            }
        })
    }

    private fun observeSaved(){
        invoiceViewModel.registerInvoiceSaved.observe(requireActivity(), Observer {
            val saved = it ?: return@Observer
            binding.loading.hide()

            if (saved.error != null) {
                requireContext().showFailed(saved.error)
            }

            if (saved.success != null) {
                navigate.invoiceRegisterToInvoiceList(requireView())
            }
        })
    }
}
