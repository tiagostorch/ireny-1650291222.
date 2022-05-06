package com.ireny.nf_control_mei.data.repositories

import androidx.annotation.WorkerThread
import com.ireny.nf_control_mei.data.room.dao.UserDao
import com.ireny.nf_control_mei.data.room.entities.User
import java.lang.Exception

class UserRepository(private val userDao: UserDao){

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getBy(uid: String): Result<User> {
        runCatching {
            val results = userDao.getByUid(uid)
            return if(results.isNotEmpty()){
                Result.Success(data = results[0])
            }else {
                Result.Error(exception = Exception("no found data"))
            }
        }.getOrElse {
            return Result.Error(exception = Exception(it))
        }
    }
}