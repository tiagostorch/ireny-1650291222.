package com.ireny.nf_control_mei.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "company")
data class Company (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "company_id")
    val companyId: Long? = null,
    @ColumnInfo(name = "company_name") val companyName: String,
    @ColumnInfo(name = "corporate_name") val corporateName: String,
    @ColumnInfo(name = "company_identifier") val companyIdentifier: String
)