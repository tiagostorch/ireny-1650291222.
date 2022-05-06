package com.ireny.nf_control_mei.di

import com.ireny.nf_control_mei.data.LocalDataStore
import com.ireny.nf_control_mei.data.repositories.*
import com.ireny.nf_control_mei.data.room.AppRoomDatabase
import com.ireny.nf_control_mei.data.room.dao.UserDao
import com.ireny.nf_control_mei.navigation.*
import com.ireny.nf_control_mei.ui.base.BaseViewModel
import com.ireny.nf_control_mei.ui.invoice.InvoiceViewModel
import com.ireny.nf_control_mei.ui.login.LoginViewModel
import com.ireny.nf_control_mei.ui.register_user.RegisterUserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.expm1


val appModule = module {

    single { LocalDataStore(androidContext()) }

    factory {
        AppRoomDatabase.getDatabase(androidContext()).userDao()
    }

    factory {
        AppRoomDatabase.getDatabase(androidContext()).companyDao()
    }

    factory {
        AppRoomDatabase.getDatabase(androidContext()).expenseTypeDao()
    }

    factory {
        AppRoomDatabase.getDatabase(androidContext()).invoiceDao()
    }

    factory {
        AppRoomDatabase.getDatabase(androidContext()).expenseDao()
    }

    factory { UserRepository(userDao = get()) }
    factory { CompanyRepository(companyDao = get()) }
    factory { ExpenseTypeRepository(expenseTypeDao = get()) }
    factory { InvoiceRepository(invoiceDao = get()) }
    factory { ExpenseRepository(expenseDao = get()) }

    viewModel {
        LoginViewModel(localDataStore = get())
    }

    viewModel {
        RegisterUserViewModel(repository = get())
    }

    viewModel {
        BaseViewModel(
            companyRepository = get(),
            expenseTypeRepository = get(),
            localDataStore = get(),
            invoiceRepository = get()
        )
    }

    viewModel {
        InvoiceViewModel(invoiceRepository = get())
    }

    single { SplashNavigation() }
    single { LoginNavigation() }
    single { HomeNavigation() }
    single { RegisterUserNavigation() }
    single { InvoiceListNavigation() }
    single { InvoiceRegisterNavigation() }
    single { PreferencesNavigation() }
}
