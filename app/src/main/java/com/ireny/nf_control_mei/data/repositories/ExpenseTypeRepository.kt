package com.ireny.nf_control_mei.data.repositories

import androidx.annotation.WorkerThread
import com.ireny.nf_control_mei.data.room.dao.ExpenseTypeDao
import com.ireny.nf_control_mei.data.room.entities.ExpenseType
import kotlinx.coroutines.flow.Flow

class ExpenseTypeRepository(private val expenseTypeDao: ExpenseTypeDao) {

    val allExpenseTypes: Flow<List<ExpenseType>> = expenseTypeDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: ExpenseType) {
        expenseTypeDao.insert(entity)
    }

    @WorkerThread
    suspend fun countAll(): Int {
        return expenseTypeDao.countAll()
    }
}