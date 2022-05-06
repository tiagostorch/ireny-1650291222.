package com.ireny.nf_control_mei.data.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "expense",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseType::class,
            parentColumns = ["expense_type_id"],
            childColumns = ["expense_type_fk_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["company_id"],
            childColumns = ["company_fk_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["expense_type_fk_id"]),
        Index(value = ["company_fk_id"])
    ]
)
data class Expense (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    val expenseId: Long? = null,
    @Embedded val expenseType: ExpenseType,
    @ColumnInfo(name = "expense_type_fk_id") val expenseTypeFkId: Long,
    @Embedded val company: Company? = null,
    @ColumnInfo(name = "company_fk_id") val companyFkId: Long? = null,
    @ColumnInfo(name = "expense_value") val expenseValue: Double,
    @ColumnInfo(name = "expense_name") val expenseName: String,
    @ColumnInfo(name = "payment_at") val paymentAt: Date,
    @ColumnInfo(name = "reference_at") val referenceAt: Date
)