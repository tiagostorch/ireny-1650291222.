package com.ireny.nf_control_mei.ui.register_user

import com.ireny.nf_control_mei.data.room.entities.User
import com.ireny.nf_control_mei.ui.base.ProcessResult

class UserResult(success:User? = null, error:Int? = null): ProcessResult<User?>(success = success, error = error)