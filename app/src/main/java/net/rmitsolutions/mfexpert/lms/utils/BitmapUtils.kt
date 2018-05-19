package net.rmitsolutions.mfexpert.lms.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.util.IslamicCalendar
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_camera.*
import net.rmitsolutions.mfexpert.lms.helpers.logD
import java.io.IOException


object BitmapUtils {

    fun getBase64StringFromBitmap(bitmap : Bitmap, quality: Int): String? {
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayStream)
        val byteArray = byteArrayStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getBitmapFromBase64String(base64String : String): Bitmap? {
        val bitmapArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        return if (bitmap !=null){
            bitmap
        }else {
            null
        }
    }

    fun getBitmapToByteArray(bitmap: Bitmap, quality : Int): ByteArray? {
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayStream)
        return byteArrayStream.toByteArray()
    }

    fun getByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return if (bitmap !=null){
            bitmap
        }else {
            null
        }
    }


    fun decodeBitmapFromPath(currentPath : String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val bitmapFile = File(currentPath)
        return if (bitmapFile.exists()){
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(bitmapFile.absolutePath, options)

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            options.inJustDecodeBounds = false
            BitmapFactory.decodeFile(bitmapFile.absolutePath, options)
        }else{
            null
        }
    }

    fun rotateImageView(imageView : ImageView){
        if (imageView.rotation == 0.0F){
            imageView.rotation += 90
            return
        }
        imageView.rotation -= 90
    }


//    @Throws(IOException::class)
//    fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {
//        val input = context.contentResolver.openInputStream(selectedImage)
//        val ei: ExifInterface
//        ei = if (Build.VERSION.SDK_INT > 23)
//            ExifInterface(input)
//        else
//            ExifInterface(selectedImage.path)
//
//        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//
//        Log.d("Orientation", "$orientation")
//        Log.d("Normal", ExifInterface.ORIENTATION_NORMAL.toString())
//        Log.d("Roatate 90", ExifInterface.ORIENTATION_ROTATE_90.toString())
//        Log.d("Rotate 180", ExifInterface.ORIENTATION_ROTATE_180.toString())
//        if (orientation == 0){
//            rotateImage(img, 90)
//        }else {
//            Log.d("Error", "Image orientation is not set to Normal")
//        }
//        return img
//
////        return when (orientation) {
////            ExifInterface.ORIENTATION_NORMAL -> rotateImage(img, 90)
////            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
////            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
////            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
////            else -> img
////        }
//    }


    fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                        reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun progressValidation(progress : Int): Int? {
        if (progress in 0..10){
            return 10
        }else if (progress in 10..20){
            return 20
        }else if (progress in 20..30){
            return 30
        }else if(progress in 30..40){
            return 40
        }else if(progress in 40..50){
            return 50
        }else if(progress in 50..60){
            return 60
        }else if (progress in 60..70){
            return 70
        }else if (progress in 70..80){
            return 80
        }else if (progress in 80..90){
            return 90
        }else if (progress in 90..100){
            return 100
        }else {
            return null
        }
    }


}