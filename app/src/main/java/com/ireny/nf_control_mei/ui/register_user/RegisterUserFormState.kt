package com.ireny.nf_control_mei.ui.register_user

class RegisterUserFormState (
    var usernameError: Int? = null,
    var passwordError: Int? = null,
    var passwordRepeatError: Int? = null,
    var nameError: Int? = null,
    var companyIdentifyError: Int? = null,
    var companyNameError: Int? = null,
    var phoneError: Int? = null,
    var isDataValid: Boolean = false
)