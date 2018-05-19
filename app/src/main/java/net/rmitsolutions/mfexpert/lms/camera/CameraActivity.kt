package net.rmitsolutions.mfexpert.lms.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_camera.*
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.Constants.CAMERA_FILE_PROVIDER
import net.rmitsolutions.mfexpert.lms.Constants.DEFAULT_IMAGE_QUALITY
import net.rmitsolutions.mfexpert.lms.Constants.DEFAULT_SYNC_STATE
import net.rmitsolutions.mfexpert.lms.Constants.SEEKBAR_MAX_VALUE
import net.rmitsolutions.mfexpert.lms.Constants.showToast
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.entities.ImageDetails
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.showToast
import net.rmitsolutions.mfexpert.lms.utils.BitmapUtils.decodeBitmapFromPath
import net.rmitsolutions.mfexpert.lms.utils.BitmapUtils.getBitmapToByteArray
import net.rmitsolutions.mfexpert.lms.utils.BitmapUtils.progressValidation
import net.rmitsolutions.mfexpert.lms.utils.BitmapUtils.rotateImageView
import java.io.File
import java.io.IOException
import java.util.*

class CameraActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    private var cameraImage: File? = null
    private var imageFileName: String? = null
    private var filePrefix: String? = null
    private var mCurrentPhotoPath: String? = null
    private var cameraImageUri: Uri? = null
    private var cropImageUri: Uri? = null
    private val CAMERA_REQUEST_CODE = 100
    private var bitmap: Bitmap? = null
    private lateinit var seekBar: SeekBar
    private var seekProgress: Int? = -1
    private var rotate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        seekBar = findViewById(R.id.seekBar)
        seekBar.max = SEEKBAR_MAX_VALUE
        seekBar.progress = DEFAULT_IMAGE_QUALITY
        textViewProgress.text = "Quality ${seekBar.progress}/$SEEKBAR_MAX_VALUE"

        seekBar.setOnSeekBarChangeListener(this)

        filePrefix = intent.getStringExtra("FilePrefix")
        if (filePrefix == null) {
            filePrefix = "Image_"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_camera_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.camera -> {
                rotate = false
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    try {
                        cropImageUri = null
                        cameraImage = createImageFile()
                        if (cameraImage != null) {
                            cameraImageUri = FileProvider.getUriForFile(this, CAMERA_FILE_PROVIDER, cameraImage!!)
                            logD("Camera Image Uri : $cameraImageUri")
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        }
                    } catch (e: IOException) {
                        logE("Error : $e")
                    }
                }
                return true
            }
            R.id.crop -> {
                rotate = false
                if (cameraImageUri != null && bitmap != null) {
                    CropImage.activity(cameraImageUri).start(this);
                } else {
                    showToast("Please select an Image")
                }
                return true
            }
            R.id.save -> {
                if (bitmap != null) {
                    getBitmapDetails(bitmap!!)
                } else {
                    showToast("Please select an Image")
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    seekBar.progress = DEFAULT_IMAGE_QUALITY
                    bitmap = BitmapFactory.decodeFile(cameraImage?.absolutePath)
                    if (bitmap != null) {
                        Glide.with(this).load(bitmap).into(cameraImageView)
                        if (cameraImageView.rotation == 0.0F) {
                            rotateImageView(cameraImageView)
                        }
                    } else {
                        showToast("Some error occurred to load bitmap!")
                    }
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri.path
                    mCurrentPhotoPath = resultUri
                    logD("Result Uri : $resultUri")
                    bitmap = decodeBitmapFromPath(resultUri.toString(), 200, 500)
                    Glide.with(this).load(bitmap).into(cameraImageView)
                    if (cameraImageView.rotation != 0.0F) {
                        rotateImageView(cameraImageView)
                    }
                }
            }
        }
    }


    private fun createImageFile(): File? {
//        val timeStamp = System.currentTimeMillis()
        imageFileName = filePrefix
        logD("Image File Name : $imageFileName")
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun getBitmapDetails(bitmap: Bitmap) {
        val file = File(mCurrentPhotoPath)
        val imageDetails = ImageDetails()
        if (file.exists()) {
            val imageInfo = ImageInformation()
            val data = imageInfo.getImageInformation(mCurrentPhotoPath!!)
            logD("\nLatitude : ${data?.latitude} \nLongitude : ${data?.longitude}\nDate Time Take Photo : ${data?.dateTimeTakePhoto}\n" +
                    "Date : ${data?.dateStamp}\nImage Length : ${data?.imageLength}\nImage Width : ${data?.imageWidth}\nModel : ${data?.modelDevice}\nMaker : ${data?.makeCompany}")
            imageDetails.imageFileName = imageFileName
            imageDetails.latitude = data?.latitude
            imageDetails.longitude = data?.longitude
            imageDetails.captureTime = data?.dateStamp
            imageDetails.syncState = DEFAULT_SYNC_STATE
            if (seekProgress!! < DEFAULT_IMAGE_QUALITY) {
                seekProgress = DEFAULT_IMAGE_QUALITY
            }
            imageDetails.imageArray = getBitmapToByteArray(bitmap, seekProgress!!)
            try {
                MfExpertApp.database.ImageDao().insertImageDetails(imageDetails)
            } catch (e: Exception) {
                logE("Error : $e")
            }
            showToast("Image saved successfully!")
        } else {
            logE("Image Not Found!")
            showToast("Image not Found!")
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (cropImageUri == null) {
            if (mCurrentPhotoPath != null) {
                bitmap = decodeBitmapFromPath(mCurrentPhotoPath!!, 200, 500)
                if (bitmap != null) {
                    setCompressedProgress(bitmap!!, progress)
                } else {
                    showToast("Please select an Image!")
                }
            } else {
                logE("mCurrentPhotoPath is null")
            }
        } else {
            bitmap = decodeBitmapFromPath(mCurrentPhotoPath!!, 200, 500)
            if (bitmap != null) {
                setCompressedProgress(bitmap!!, progress)
            } else {
                logE("Some Error occurred")
            }
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (bitmap != null) {
            textViewProgress.text = "Quality ${seekBar?.progress}/$SEEKBAR_MAX_VALUE"
            seekProgress = seekBar?.progress
        } else {
            showToast("Please capture an image to set quality")
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (bitmap != null) {
            textViewProgress.text = "Quality ${seekBar?.progress}/$SEEKBAR_MAX_VALUE"
            seekProgress = seekBar?.progress
        } else {
            showToast("Please capture an image to set quality")
        }
    }

    private fun setCompressedProgress(bitmap: Bitmap, progress: Int) {
        textViewProgress.text = "Quality $progress/$SEEKBAR_MAX_VALUE"
        seekProgress = progress
        seekProgress(bitmap, seekProgress!!)

    }

    private fun seekProgress(bitmap: Bitmap, seekProgress : Int){
        val byteArray = getBitmapToByteArray(bitmap, seekProgress)
        Glide.with(this).load(byteArray).into(cameraImageView)
    }

}
