package com.ireny.nf_control_mei.utils

import android.text.Editable
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*


class CurrencyTextWatcher(private val editText: EditText): TextWatcher {

    private var currencyInstance: Currency = Currency.getInstance(Locale.getDefault())
    private var numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    private var symbol: String = currencyInstance.getSymbol(Locale.getDefault())

    private var showSymbol = true
    private var isPercent = true
    private var currentText: String = EMPTY_STRING

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

        var text = editText.text.toString()

        if (text.isEmpty() || text == currentText) return

        text = format(returnOnlyNumbers(text))

        currentText = text
        editText.removeTextChangedListener(this)
        editText.setText(text)
        if (isPercent) {
            if (text.length <= 4) editText.setSelection(text.length)
        } else {
            editText.setSelection(text.length)
        }
        editText.addTextChangedListener(this)
    }

    override fun afterTextChanged(editable: Editable?) {
        editText.setSelection(editText.text.length)
    }

    private fun format(text: String): String {
        return try {
            val numberTen = 10
            val parsedDouble = text.toDouble() /
                    Math.pow(
                        numberTen.toDouble(),
                        currencyInstance!!.defaultFractionDigits.toDouble()
                    )
            if (showSymbol) {
                numberFormat.format(parsedDouble)
                    .replace("\\s+".toRegex(), EMPTY_STRING)
                    .replace(symbol, String.format("%s ", symbol))
            } else numberFormat.format(parsedDouble).replace("[^0-9,.]".toRegex(), EMPTY_STRING)
        } catch (e: Exception) {
            EMPTY_STRING
        }
    }

    private fun returnOnlyNumbers(value: String): String {
        return if (isEmpty(value)) {
            EMPTY_STRING
        } else value.replace("\\D+".toRegex(), EMPTY_STRING)
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}