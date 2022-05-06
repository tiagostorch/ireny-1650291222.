package com.ireny.nf_control_mei.ui.base

class SaveResult(success:Boolean? = null, error:Int? = null): ProcessResult<Boolean?>(success = success, error = error)