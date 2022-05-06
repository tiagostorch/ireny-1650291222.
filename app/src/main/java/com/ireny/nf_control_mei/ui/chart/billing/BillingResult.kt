package com.ireny.nf_control_mei.ui.chart.billing

import com.ireny.nf_control_mei.ui.base.ProcessResult

class BillingResult(success:BillingData? = null, error:Int? = null): ProcessResult<BillingData?>(success = success, error = error)