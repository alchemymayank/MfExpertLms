package net.rmitsolutions.mfexpert.lms.camera

import android.media.ExifInterface
import net.rmitsolutions.mfexpert.lms.Constants.notNullNotFill
import net.rmitsolutions.mfexpert.lms.models.ModelImageInfo
import java.io.IOException

class ImageInformation {

    private var modelImageInfo : ModelImageInfo

    init {
        modelImageInfo = ModelImageInfo()
    }

    private fun getAllFeatures(realPath: String): ExifInterface? {
        if (realPath != "") {
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(realPath)
                return exif
            } catch (e: IOException) {
                return exif
            }
        } else {
            return null
        }
    }

    fun getImageInformation(path : String): ModelImageInfo? {
        val exif = getAllFeatures(path)
        if (exif!=null){
            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE))){
                modelImageInfo.latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE))){
                modelImageInfo.longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_DATETIME))){
                modelImageInfo.dateStamp = exif.getAttribute(ExifInterface.TAG_DATETIME)
            }
            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH))){
                modelImageInfo.imageLength = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
            }
            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH))){
                modelImageInfo.imageWidth = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL))){
                modelImageInfo.dateStamp = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_ISO))){
                modelImageInfo.iso = exif.getAttribute(ExifInterface.TAG_ISO)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_MAKE))){
                modelImageInfo.makeCompany = exif.getAttribute(ExifInterface.TAG_MAKE)
            }

            if (notNullNotFill(exif.getAttribute(ExifInterface.TAG_MODEL))){
                modelImageInfo.modelDevice = exif.getAttribute(ExifInterface.TAG_MODEL)
            }

            return modelImageInfo
        }else{
            return null
        }
    }
}