package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.home.HomeFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class HomeNavigation {

    fun homeToSplash(view: View) {
        view.navigateTo(
            HomeFragmentDirections.actionHomeFragmentToSplashFragment()
        )
    }

    fun homeToInvoiceList(view: View) {
        view.navigateTo(
            HomeFragmentDirections.actionHomeFragmentToInvoiceListFragment()
        )
    }

    fun homeToInvoiceRegister(view: View) {
        view.navigateTo(
            HomeFragmentDirections.actionHomeFragmentToInvoiceRegisterFragment(invoiceId = 0)
        )
    }

    fun homeToPreferences(view: View) {
        view.navigateTo(
            HomeFragmentDirections.actionHomeFragmentToPreferencesFragment()
        )
    }
}