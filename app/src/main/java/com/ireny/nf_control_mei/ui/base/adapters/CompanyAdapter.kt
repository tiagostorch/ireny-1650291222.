package com.ireny.nf_control_mei.ui.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ireny.nf_control_mei.data.room.entities.Company
import com.ireny.nf_control_mei.databinding.SimpleItemBinding
import com.ireny.nf_control_mei.ui.base.adapters.CompanyAdapter.*

class CompanyAdapter(private val click: (Company) -> Unit): RecyclerView.Adapter<ViewHolder>() {

    private var values: List<Company> = arrayListOf()
    var filteredValues : List<Company> = values
    var filter: String = ""

    fun updateList(data: List<Company>){
        values = data
        filteredValues = values
        executeFilter()
    }

    fun filter(search:String){
        filter = search
        executeFilter()
    }

    private fun executeFilter(){
        filteredValues = values.filter { it.companyIdentifier.startsWith(filter) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SimpleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredValues[position]
        holder.description.text = "${item.companyIdentifier} ${item.companyName}"
        holder.itemView.setOnClickListener {
            click.invoke(item)
        }
    }

    override fun getItemCount(): Int = filteredValues.size

    inner class ViewHolder(binding: SimpleItemBinding) :RecyclerView.ViewHolder(binding.root) {
        val description : TextView = binding.itemDescription
    }
}