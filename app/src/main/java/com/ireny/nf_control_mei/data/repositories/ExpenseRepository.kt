package com.ireny.nf_control_mei.data.repositories

import androidx.annotation.WorkerThread
import com.ireny.nf_control_mei.data.room.dao.ExpenseDao
import com.ireny.nf_control_mei.data.room.entities.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses: Flow<List<Expense>> = expenseDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: Expense) {
        expenseDao.insert(entity)
    }
}