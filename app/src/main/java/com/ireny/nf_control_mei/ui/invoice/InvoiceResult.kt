package com.ireny.nf_control_mei.ui.invoice

import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.ui.base.ProcessResult

class InvoiceResult(success: Invoice? = null, error:Int? = null): ProcessResult<Invoice?>(success = success, error = error)