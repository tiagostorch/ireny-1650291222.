package com.ireny.nf_control_mei.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ireny.nf_control_mei.data.room.entities.Company
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company ORDER BY company_name")
    fun getAll(): Flow<List<Company>>

    @Query("SELECT IFNULL(COUNT(*),0) FROM company")
    suspend fun countAll(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: Company)
}