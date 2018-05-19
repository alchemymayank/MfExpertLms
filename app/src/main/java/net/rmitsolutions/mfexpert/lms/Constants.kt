package net.rmitsolutions.mfexpert.lms

import android.content.Context
import android.util.Log
import android.widget.Toast
import net.openid.appauth.ResponseTypeValues
import net.rmitsolutions.appauthwebview.AppAuthWebViewData
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object Constants {
//    const val BASE_URL = "http://10.0.2.2:5000/"
//    const val API_BASE_URL = "http://10.0.2.2:6002/api/"

    const val BASE_URL = "http://183.82.2.126:5000/"
    const val API_BASE_URL = "http://183.82.2.126:6002/api/"

    const val CONNECTION_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60

    val DEFAULT_SYNC_STATE: Byte = 0

    val DISPLAY_FULL_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss a"

    val DEFAULT_IMAGE_QUALITY = 60
    val SEEKBAR_MAX_VALUE = 100
    val CAMERA_FILE_PROVIDER = "net.rmitsolutions.mfexpert.lms.provider"


    var ACCESS_TOKEN: String? = null

    fun getAppAuthWebViewData(): AppAuthWebViewData {
        val data = AppAuthWebViewData()
        data.clientId = "lmsMobile"
        data.authorizationEndpointUri = BASE_URL + "connect/authorize"
        data.clientSecret = ""
        data.discoveryUri = "n"
        data.redirectLoginUri = BASE_URL + "Home/LoginSuccess"
        data.redirectLogoutUri = BASE_URL + "Home/LogoutSuccess"
        data.scope = "openid profile offline_access lmsApi"
        data.tokenEndpointUri = BASE_URL + "connect/token"
        data.endSessionEndpointUri = BASE_URL + "connect/endsession"
        data.registrationEndpointUri = "n"
        data.responseType = ResponseTypeValues.CODE
        data.isGenerateCodeVerifier = true
        return data
    }

    fun logE(tag: String, message: String){
        Log.e(tag, message)
    }

    fun logD(tag: String, message: String){
        Log.d(tag, message)
    }

    //validate if the string isnull or empty
    fun notNullNotFill(validate: String?): Boolean {
        return if (validate != null) {
            validate.trim { it <= ' ' } != ""
        } else {
            false
        }
    }

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }



}