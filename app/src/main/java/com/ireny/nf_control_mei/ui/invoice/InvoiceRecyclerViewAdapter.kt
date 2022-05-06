package com.ireny.nf_control_mei.ui.invoice

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.databinding.FragmentInvoiceItemBinding
import com.ireny.nf_control_mei.utils.toDateTextFormatted
import com.ireny.nf_control_mei.utils.toRealFormat
import java.text.NumberFormat
import java.util.*

class InvoiceRecyclerViewAdapter(private val values: List<Invoice>, private val click: (Invoice) -> Unit) : RecyclerView.Adapter<InvoiceRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentInvoiceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.number.text = item.invoiceNumber
        holder.value.text = item.invoiceValue.toRealFormat()
        holder.month.text = "${item.month}/${item.year}"
        holder.received.text = item.receivedAt.toDateTextFormatted()
        holder.itemView.setOnClickListener {
            click.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentInvoiceItemBinding) :RecyclerView.ViewHolder(binding.root) {
        val number: TextView = binding.itemNumber
        val value: TextView = binding.itemValue
        val month: TextView = binding.itemMonth
        val received: TextView = binding.itemReceived
    }

}