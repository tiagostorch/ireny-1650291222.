package com.ireny.nf_control_mei.ui.base

abstract class ProcessResult<T> (
    val success: T? = null,
    val error: Int? = null
)