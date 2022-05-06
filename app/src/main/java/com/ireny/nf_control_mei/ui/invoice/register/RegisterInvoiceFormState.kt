package com.ireny.nf_control_mei.ui.invoice.register

class RegisterInvoiceFormState (
    var numberError: Int? = null,
    var valueError: Int? = null,
    var monthError: Int? = null,
    var receivedError: Int? = null,
    var descriptionError: Int? = null,
    var isDataValid: Boolean = false
)