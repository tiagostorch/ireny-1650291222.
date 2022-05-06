package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.invoice.register.InvoiceRegisterFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class InvoiceRegisterNavigation {

    fun invoiceRegisterToInvoiceList(view: View) {
        view.navigateTo(
            InvoiceRegisterFragmentDirections.actionInvoiceRegisterFragmentToInvoiceListFragment()
        )
    }
}