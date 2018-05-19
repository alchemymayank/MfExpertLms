package net.rmitsolutions.mfexpert.lms.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import net.rmitsolutions.mfexpert.lms.database.dao.ImageDao
import net.rmitsolutions.mfexpert.lms.database.entities.ImageDetails
import net.rmitsolutions.mfexpert.lms.sample.Users
import net.rmitsolutions.mfexpert.lms.sample.dao.UserDao

/**
 * Created by Madhu on 24-Apr-2018.
 */
@Database(entities = [(Users::class), (ImageDetails::class)], version = 1)
abstract class MfExpertLmsDatabase : RoomDatabase() {
    //abstract fun studentDao(): StudentDao
    //abstract fun schoolDao(): SchoolDao
    abstract fun userDao() : UserDao
    abstract fun ImageDao() : ImageDao
}
