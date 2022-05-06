package com.ireny.nf_control_mei.data.repositories

import androidx.annotation.WorkerThread
import com.ireny.nf_control_mei.data.room.dao.CompanyDao
import com.ireny.nf_control_mei.data.room.entities.Company
import kotlinx.coroutines.flow.Flow

class CompanyRepository(private val companyDao: CompanyDao) {

    val allCompanies: Flow<List<Company>> = companyDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: Company) {
        companyDao.insert(entity)
    }

    @WorkerThread
    suspend fun countAll(): Int {
        return companyDao.countAll()
    }
}