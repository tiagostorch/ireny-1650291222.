package com.ireny.nf_control_mei.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ireny.nf_control_mei.data.room.converters.DateConverter
import com.ireny.nf_control_mei.data.room.dao.*
import com.ireny.nf_control_mei.data.room.entities.*

@Database(
    entities = [
        User::class,
        Company::class,
        ExpenseType::class,
        Invoice::class,
        Expense::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun expenseTypeDao(): ExpenseTypeDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "nf_control_mei_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}