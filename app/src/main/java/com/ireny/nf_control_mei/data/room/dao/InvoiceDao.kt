package com.ireny.nf_control_mei.data.room.dao

import androidx.room.*
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.data.room.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoice ORDER BY received_at DESC")
    fun getAll(): Flow<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: Invoice)

    @Query("SELECT * FROM invoice WHERE invoice_id LIKE :id")
    suspend fun getById(id: Long): List<Invoice>

    @Delete
    suspend fun delete(entity: Invoice)

    @Update
    suspend fun update(invoice: Invoice)

    @Query("SELECT IFNULL(SUM(invoice_value),0) FROM invoice WHERE year LIKE :year")
    suspend fun getTotal(year: String = Calendar.getInstance().get(Calendar.YEAR).toString()): Double
}