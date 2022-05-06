package com.ireny.nf_control_mei.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "user")
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "provider")  val provider: String,
    val email: String,
    val password: String?,
    val name: String,
    @ColumnInfo(name = "company_identifier") val companyIdentifier: String,
    @ColumnInfo(name = "company_name") val companyName: String,
    val phone: String
)