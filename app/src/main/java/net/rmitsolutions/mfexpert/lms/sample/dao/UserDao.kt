package net.rmitsolutions.mfexpert.lms.sample.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.sample.Users

@Dao
interface UserDao {

    @Insert
    fun insertUser(users: Users)

    @Query("select * from users")
    fun getAllUsers() : List<Users>

}