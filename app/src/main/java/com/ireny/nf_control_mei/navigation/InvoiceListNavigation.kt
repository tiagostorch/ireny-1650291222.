package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.invoice.InvoiceListFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class InvoiceListNavigation {

    fun invoiceListToInvoiceRegister(view: View, invoiceId: Long = 0) {
        view.navigateTo(
            InvoiceListFragmentDirections.actionInvoiceListFragmentToInvoiceRegisterFragment(invoiceId = invoiceId)
        )
    }

    fun invoiceListToHome(view: View) {
        view.navigateTo(
            InvoiceListFragmentDirections.actionInvoiceListFragmentToHomeFragment()
        )
    }
}