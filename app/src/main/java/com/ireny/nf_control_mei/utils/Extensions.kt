package com.ireny.nf_control_mei.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.ireny.nf_control_mei.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


inline fun <F : Fragment, reified V : Any> F.navDirections() = inject<V> {
    parametersOf(this)
}

fun View.navigateTo(directions: NavDirections, navOptions: NavOptions? = null) = try {
    findNavController().navigate(directions, navOptions)
} catch (e: IllegalArgumentException) {
    e.printStackTrace()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


fun EditText.maskedText(mask: String,afterTextChanged: ((String) -> Unit)? = null) {
    this.addTextChangedListener(MaskedTextChangedListener(mask, this))
    afterTextChanged?.let {
        this.afterTextChanged(it)
    }
}


fun EditText.currencyText(afterTextChanged: ((String) -> Unit)? = null) {
    this.addTextChangedListener(CurrencyTextWatcher(this))
    afterTextChanged?.let {
        this.afterTextChanged(it)
    }
}

fun String.toCurrencyDouble(): Double? {
    runCatching {
        return  Regex("[^0-9,]").replace(this, "").replace(',', '.').toDouble()
    }.getOrElse {
        return null
    }
}

fun Date?.toDateTextFormatted():String{
    this?.run {
        val formatter = SimpleDateFormat("dd/MM/yyyy",Locale("pt", "BR"))
        return formatter.format(this)
    }
    return ""
}

fun String.toDate():Date?{
    if(this.isBlank()){
        return null
    }
    val formatter = SimpleDateFormat("dd/MM/yyyy",Locale("pt", "BR"))
    return formatter.parse(this)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showOrHide() {
    if(this.visibility == View.GONE || this.visibility == View.INVISIBLE){
        this.visibility = View.VISIBLE
    }else {
        this.visibility = View.GONE
    }
}

fun Context.openDialog(title: String,
                       message: String,
                       includeNeutralButton: Boolean,
                       cancelFun: (() -> Unit)? = null,
                       confirmFun: (() -> Unit)?
){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton(if(confirmFun != null) R.string.confirm else R.string.dialog_button_ok){ _, _ ->
        confirmFun?.invoke()
    }
    if(includeNeutralButton) {
        builder.setNeutralButton(R.string.cancel) { _, _ ->
            cancelFun?.invoke()
        }
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun Context.showFailed(@StringRes errorString: Int) {
    Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
}

fun Double.toRealFormat(): String{
  return  NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
}

fun Float.toRealFormat(): String{
    return  NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
}


