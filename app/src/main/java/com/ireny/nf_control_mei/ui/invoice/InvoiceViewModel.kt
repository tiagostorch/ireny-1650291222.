package com.ireny.nf_control_mei.ui.invoice

import androidx.lifecycle.*
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.data.repositories.InvoiceRepository
import com.ireny.nf_control_mei.data.repositories.Result
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.ui.base.SaveResult
import com.ireny.nf_control_mei.ui.invoice.register.RegisterInvoiceFormState
import com.ireny.nf_control_mei.ui.register_user.UserResult
import com.ireny.nf_control_mei.utils.toCurrencyDouble
import com.ireny.nf_control_mei.utils.toDate
import kotlinx.coroutines.launch
import java.util.*

class InvoiceViewModel(private val invoiceRepository: InvoiceRepository): ViewModel() {

    val allInvoices: LiveData<List<Invoice>> = invoiceRepository.allInvoices.asLiveData()

    private val _registerInvoiceFormState = MutableLiveData<RegisterInvoiceFormState>()
    val registerInvoiceFormState: LiveData<RegisterInvoiceFormState> = _registerInvoiceFormState

    private val _registerInvoiceSaved = MutableLiveData<SaveResult>()
    val registerInvoiceSaved: LiveData<SaveResult> = _registerInvoiceSaved

    private val _invoiceResult = MutableLiveData<InvoiceResult>()
    val invoiceResult: LiveData<InvoiceResult> = _invoiceResult

    fun getInvoice(invoiceId: Long){
        viewModelScope.launch {
            runCatching {
                val result = invoiceRepository.getById(invoiceId)
                if (result is Result.Success) {
                    _invoiceResult.value = InvoiceResult(success = result.data)
                } else {
                    _invoiceResult.value = InvoiceResult(error = R.string.invoice_not_found)
                }
            }.getOrElse {
                _invoiceResult.value = InvoiceResult(error = R.string.invoice_not_found)
            }
        }
    }


    fun delete(invoice: Invoice) {
        viewModelScope.launch {
            runCatching {
                invoiceRepository.delete(invoice)
                _registerInvoiceSaved.value = SaveResult(true)
            }.getOrElse {
                _registerInvoiceSaved.value = SaveResult(error = R.string.delete_fail)
            }
        }
    }

    private fun insert(invoice: Invoice){
        viewModelScope.launch {
            runCatching {
                invoiceRepository.insert(invoice)
                _registerInvoiceSaved.value = SaveResult(true)
            }.getOrElse {
                _registerInvoiceSaved.value = SaveResult(error = R.string.save_failed)
            }
        }
    }

    private fun update(invoice: Invoice){
        viewModelScope.launch {
            runCatching {
                invoiceRepository.update(invoice)
                _registerInvoiceSaved.value = SaveResult(true)
            }.getOrElse {
                _registerInvoiceSaved.value = SaveResult(error = R.string.save_failed)
            }
        }
    }

    fun save(value: String,
             number: String,
             month: String,
             received: String,
             description: String,
             company: Company,
             invoice:Invoice? = null
    )
    {
        if(invoice == null) {
            val newInvoice = Invoice(
                company = company,
                companyFkId = company.companyId!!,
                invoiceValue = value.toCurrencyDouble()!!,
                invoiceNumber = number,
                invoiceDescription = description,
                month = month.padStart(2, '0'),
                receivedAt = received.toDate() ?: Date()
            )
            insert(newInvoice)
        } else {
            invoice.company = company
            invoice.companyFkId = company.companyId!!
            invoice.invoiceValue = value.toCurrencyDouble()!!
            invoice.invoiceNumber = number
            invoice.invoiceDescription = description
            invoice.month = month.padStart(2, '0')
            invoice.receivedAt = received.toDate() ?: Date()
            update(invoice)
        }
    }

    fun formDataChanged(value: String,
                        number: String,
                        month: String,
                        received: String,
                        description: String)
    {
        val state = RegisterInvoiceFormState()
        var isValid = true

        if(value.isBlank()){
            state.valueError = R.string.required_field
            isValid = false
        }else{
            if(!invoiceValueValid(value)){
                state.valueError = R.string.invalid_field
                isValid = false
            }
        }

        if(number.isBlank()){
            state.numberError = R.string.required_field
            isValid = false
        }

        if(month.isBlank()){
            state.monthError = R.string.required_field
            isValid = false
        } else {
             if(!monthValid(month)){
                 state.monthError = R.string.invalid_field
                 isValid = false
             }
        }

        if(received.isBlank()){
            state.receivedError = R.string.required_field
            isValid = false
        } else {
            if(!receivedDateValid(received)) {
                state.receivedError = R.string.invalid_field
                isValid = false
            }
        }

        if(description.isBlank()){
            state.descriptionError = R.string.required_field
            isValid = false
        }

        state.isDataValid = isValid
        _registerInvoiceFormState.value = state
    }

    private fun invoiceValueValid(value: String): Boolean {
        return value.toCurrencyDouble() != null
    }

    private fun receivedDateValid(received: String): Boolean {
        runCatching {
            val d = received.toDate()
            d?.run {
                return d.before(Date())
            }
            return false
        }.getOrElse {
            return false
        }
    }

    private fun monthValid(month: String): Boolean{
        runCatching {
            val d = month.toInt()
            if(d < 1 || d >12) {
                return false
            }
            val monthCurrent = Calendar.getInstance().get(Calendar.MONTH)+1
            if(d > monthCurrent){
                return false
            }
            return true
        }.getOrElse {
            return false
        }
    }

}