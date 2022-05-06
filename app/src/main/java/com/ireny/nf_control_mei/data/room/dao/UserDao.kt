package com.ireny.nf_control_mei.data.room.dao

import androidx.room.*
import com.ireny.nf_control_mei.data.room.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE uid LIKE :uid")
    suspend fun getByUid(uid: String): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: User)
}