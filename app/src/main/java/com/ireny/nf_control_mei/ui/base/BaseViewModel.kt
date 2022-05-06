package com.ireny.nf_control_mei.ui.base

import androidx.lifecycle.*
import com.ireny.nf_control_mei.R
import com.ireny.nf_control_mei.data.LocalDataStore
import com.ireny.nf_control_mei.data.repositories.CompanyRepository
import com.ireny.nf_control_mei.data.repositories.ExpenseTypeRepository
import com.ireny.nf_control_mei.data.repositories.InvoiceRepository
import com.ireny.nf_control_mei.data.repositories.Result
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.data.room.entities.ExpenseType
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.ui.chart.billing.BillingData
import com.ireny.nf_control_mei.ui.chart.billing.BillingResult
import com.ireny.nf_control_mei.ui.login.LoginFormState
import com.ireny.nf_control_mei.ui.register_user.UserResult
import com.ireny.nf_control_mei.utils.toCurrencyDouble
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class BaseViewModel(
    private val companyRepository: CompanyRepository,
    private val expenseTypeRepository: ExpenseTypeRepository,
    private val invoiceRepository: InvoiceRepository,
    private val localDataStore: LocalDataStore
): ViewModel() {

    val allCompanies: LiveData<List<Company>> = companyRepository.allCompanies.asLiveData()
    val allExpenseTypes: LiveData<List<ExpenseType>> = expenseTypeRepository.allExpenseTypes.asLiveData()

    fun mockData(){
        viewModelScope.launch {
            val countCompany = companyRepository.countAll()
            val countExpenseType = expenseTypeRepository.countAll()

            if(countCompany == 0){
                companyRepository.insert(Company(
                    companyName = "BANCO DO BRASIL SA " ,
                    corporateName = "DIRECAO GERAL" ,
                    companyIdentifier = "00.000.000/0001-91"
                ))
                companyRepository.insert(Company(
                    companyName = "GLOBO COMUNICACAO E PARTICIPACOES S/A" ,
                    corporateName = "TV/REDE/CANAIS/G2C+GLOBO GLOBO.COM GLOBOPLAY" ,
                    companyIdentifier = "27.865.757/0001-02"
                ))
                companyRepository.insert(Company(
                    companyName = "GOOGLE INTERNET S/C LTDA" ,
                    corporateName = "GOOGLE" ,
                    companyIdentifier = "03.281.783/0001-17"
                ))
                companyRepository.insert(Company(
                    companyName = "FACEBOOK SERVICOS ONLINE DO BRASIL LTDA." ,
                    corporateName = "FACEBOOK" ,
                    companyIdentifier = "13.347.016/0001-17"
                ))
            }

            if(countExpenseType == 0){
                expenseTypeRepository.insert(ExpenseType(expenseTypeName = "Manutenção"))
                expenseTypeRepository.insert(ExpenseType(expenseTypeName = "Funcionários"))
                expenseTypeRepository.insert(ExpenseType(expenseTypeName = "Despesas gerais"))
            }
        }
    }

    private val _limitMei = MutableLiveData<Float>()
    val limitMei: LiveData<Float> = _limitMei

    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult

    private val _billingResult = MutableLiveData<BillingResult>()
    val billingResult: LiveData<BillingResult> = _billingResult

    fun getBillingData(){
        runCatching {
            viewModelScope.launch {
                val limit = localDataStore.getLimitMei().toDouble()
                val year = Calendar.getInstance().get(Calendar.YEAR).toString()
                val totalResult = invoiceRepository.getTotal(year = year)
                if (totalResult is Result.Success) {
                    _billingResult.value = BillingResult(BillingData(year = year, value = totalResult.data, limit = limit))
                } else {
                    throw Exception()
                }
            }
        }.getOrElse {
            _billingResult.value = BillingResult(error = R.string.error_process)
        }
    }

    fun getLimitMei() {
        viewModelScope.launch {
            _limitMei.value = localDataStore.getLimitMei()
        }
    }

    fun saveLimitMei(limit: String){
        val value = limit.toCurrencyDouble()
        if(value != null) {
            viewModelScope.launch {
                localDataStore.setLimitMei(value)
                _saveResult.value = SaveResult(true)
            }
        }else{
            _saveResult.value = SaveResult(error = R.string.invalid_field)
        }
    }
}