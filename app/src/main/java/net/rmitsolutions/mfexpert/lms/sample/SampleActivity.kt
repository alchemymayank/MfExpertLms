package net.rmitsolutions.mfexpert.lms.sample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.helpers.logD
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.widget.EditText
import net.rmitsolutions.mfexpert.lms.databinding.ActivitySampleBinding
import org.jetbrains.anko.startActivity


class SampleActivity : BaseActivity() {

    lateinit var dataBinding:   net.rmitsolutions.mfexpert.lms.databinding.ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sample)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sample)
        dataBinding.users = Users()


        //insertUser()

    }

    fun addData(view: View){
        insertUser()
    }

    private fun insertUser() {
        val users = dataBinding.users

//        users.lastName = dataBinding.editTextLastName.text.toString()
//        users.mobileNumber = dataBinding.editTextMobileNumber.text.toString()
//        users.firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
//        users.lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
//        users.mobileNumber = findViewById<EditText>(R.id.editTextMobileNumber).text.toString()

        logD(users?.firstName)

        try {
            MfExpertApp.database.userDao().insertUser(users!!)
            logD("Insert user successfully")
        }catch(e:Exception){
            logD("Error $e")
        }

    }


    fun getRoomData(view: View){
        val intent = Intent(this, SampleData::class.java)
        startActivity(intent)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_contact
}
