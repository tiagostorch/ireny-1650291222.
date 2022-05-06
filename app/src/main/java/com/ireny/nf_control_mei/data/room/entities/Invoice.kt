package com.ireny.nf_control_mei.data.room.entities

import androidx.room.*
import java.util.*

@Entity( tableName = "invoice",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["company_id"],
            childColumns = ["company_fk_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["company_fk_id"])
    ]
)

data class Invoice (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "invoice_id")
    val invoiceId: Long? = null,
    @Embedded var company: Company,
    @ColumnInfo(name = "company_fk_id") var companyFkId: Long,
    @ColumnInfo(name = "invoice_value") var invoiceValue: Double,
    @ColumnInfo(name = "invoice_number") var invoiceNumber: String,
    @ColumnInfo(name = "invoice_description") var invoiceDescription: String,
    @ColumnInfo(name = "received_at") var receivedAt: Date,
    var month: String = Calendar.getInstance().get(Calendar.MONTH).toString().padStart(2,'0'),
    var year: String = Calendar.getInstance().get(Calendar.YEAR).toString()
)