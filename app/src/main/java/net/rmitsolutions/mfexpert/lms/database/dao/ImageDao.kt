package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.*
import net.rmitsolutions.mfexpert.lms.database.entities.ImageDetails

@Dao
interface ImageDao {
    @Insert
    fun insertImageDetails(imageDetails: ImageDetails)

    @Query("SELECT * FROM images")
    fun getAllImageDetails(): List<ImageDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllImageDetails(imageDetails: List<ImageDetails>?)

    @Update
    fun updateImageDetails(imageDetails: ImageDetails)
}