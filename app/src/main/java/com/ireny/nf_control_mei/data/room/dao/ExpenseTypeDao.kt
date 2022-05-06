package com.ireny.nf_control_mei.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ireny.nf_control_mei.data.room.entities.ExpenseType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTypeDao {
    @Query("SELECT * FROM expense_type WHERE filed == 0 ORDER BY expense_type_name")
    fun getAll(): Flow<List<ExpenseType>>

    @Query("SELECT IFNULL(COUNT(*),0) FROM expense_type")
    suspend fun countAll(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ExpenseType)
}