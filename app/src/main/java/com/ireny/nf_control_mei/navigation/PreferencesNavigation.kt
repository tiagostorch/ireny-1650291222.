package com.ireny.nf_control_mei.navigation

import android.view.View
import com.ireny.nf_control_mei.ui.preferences.PreferencesFragmentDirections
import com.ireny.nf_control_mei.utils.navigateTo

open class PreferencesNavigation {
    fun preferencesToHome(view: View) {
        view.navigateTo(
            PreferencesFragmentDirections.actionPreferencesFragmentToHomeFragment()
        )
    }
}