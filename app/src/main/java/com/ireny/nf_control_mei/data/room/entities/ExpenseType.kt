package com.ireny.nf_control_mei.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "expense_type")
data class ExpenseType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_type_id")
    val expenseTypeId: Long? = null,
    @ColumnInfo(name = "expense_type_name") val expenseTypeName: String,
    @ColumnInfo(name = "expense_type_description") val expenseTypeDescription: String? = null,
    val filed: Boolean = false
)