package net.rmitsolutions.mfexpert.lms.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "images")
class ImageDetails {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "image_file_name")
    var imageFileName: String? = null

    @ColumnInfo(name = "latitude")
    var latitude: String? = null

    @ColumnInfo(name = "longitude")
    var longitude: String? = null

    @ColumnInfo(name = "capture_time")
    var captureTime: String? = null

    @ColumnInfo(name = "sync_state")
    var syncState: Byte? = null

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var imageArray: ByteArray? = null
}