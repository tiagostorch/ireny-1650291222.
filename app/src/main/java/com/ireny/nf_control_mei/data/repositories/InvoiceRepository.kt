package com.ireny.nf_control_mei.data.repositories

import androidx.annotation.WorkerThread
import com.ireny.nf_control_mei.data.room.dao.InvoiceDao
import com.ireny.nf_control_mei.data.room.entities.Invoice
import com.ireny.nf_control_mei.data.room.entities.User
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import java.util.*

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

    val allInvoices: Flow<List<Invoice>> = invoiceDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: Invoice) {
        invoiceDao.insert(entity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getById(id: Long): Result<Invoice> {
        runCatching {
            val results = invoiceDao.getById(id)
            return if(results.isNotEmpty()){
                Result.Success(data = results[0])
            }else {
                Result.Error(exception = Exception("no found data"))
            }
        }.getOrElse {
            return Result.Error(exception = Exception(it))
        }
    }

    suspend fun delete(invoice: Invoice): Result<Any>  {
        runCatching {
            invoiceDao.delete(invoice)
            return Result.Success(data = true)
        }.getOrElse {
            return Result.Error(exception = Exception(it))
        }
    }

    suspend fun update(invoice: Invoice): Result<Invoice>{
        runCatching {
            invoiceDao.update(invoice)
            return Result.Success(data = invoice)
        }.getOrElse {
            return Result.Error(exception = Exception(it))
        }
    }

    @WorkerThread
    suspend fun getTotal(year: String = Calendar.getInstance().get(Calendar.YEAR).toString()): Result<Double> {
        runCatching {
            val sum = invoiceDao.getTotal(year)
            return Result.Success(data = sum)
        }.getOrElse {
            return Result.Error(exception = Exception(it))
        }
    }
}