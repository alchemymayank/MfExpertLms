package net.rmitsolutions.mfexpert.lms

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity

/**
 * Created by Madhu on 20-Apr-2018.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<LoginActivity>()
        finish()
        overridePendingTransition(0, 0)
    }
}