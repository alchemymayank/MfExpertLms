package net.rmitsolutions.mfexpert.lms

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.renderscript.Sampler
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.appauthwebview.AppAuthWebView
import net.rmitsolutions.mfexpert.lms.about.AboutActivity
import net.rmitsolutions.mfexpert.lms.camera.CameraActivity
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.finishNoAnim
import net.rmitsolutions.mfexpert.lms.helpers.removePref
import net.rmitsolutions.mfexpert.lms.sample.SampleActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.startActivity
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        private var backPressedTime: Long = 0
    }

    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private val mainContentFadeOutDuration = 100
    private val mainContentFadeInDuration = 200
    private val navDrawerLaunchDelay = 250

    // Primary toolbar
    private var actionBarToolbar: Toolbar? = null
    private var mainContent: View? = null

    private lateinit var handler: Handler
    internal lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler()
        compositeDisposable = CompositeDisposable()

        /* val depComponent = DaggerBaseActivityComponent.builder()
                 .applicationComponent(ESkoolApplication.applicationComponent)
                 .build()

         depComponent.injectBaseActivity(this)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        setupNavDrawer()
        setupAccount()

        mainContent = findViewById(R.id.main_content)
        if (mainContent != null) {
            mainContent!!.alpha = 0f
            mainContent!!.animate().alpha(1f).duration = mainContentFadeInDuration.toLong()
        } else {
            Timber.d("No view with ID main_content to fade in.")
        }
    }

    override fun onResume() {
        super.onResume()
        navigationView?.setCheckedItem(getSelfNavDrawerItem())
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        getActionBarToolbar()
    }

    override fun onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer()
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Snackbar.make(mainContent!!, "Tap back again to exit", Snackbar.LENGTH_SHORT).show()
            }

            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = super.onOptionsItemSelected(item)

    open fun getSelfNavDrawerItem(): Int = -1

    protected fun getActionBarToolbar(): Toolbar {
        if (actionBarToolbar == null) {
            actionBarToolbar = find<Toolbar>(R.id.toolbar_actionbar)
            if (actionBarToolbar != null) {
                actionBarToolbar!!.navigationContentDescription = resources.getString(R.string.nav_drawer_description)
                setSupportActionBar(actionBarToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                actionBarToolbar!!.setNavigationOnClickListener {
                    finishAfterTransition()
                }
            }
        }
        return actionBarToolbar!!
    }

    private fun setupNavDrawer() {
        drawerLayout = findOptional(R.id.drawer_layout)
        if (drawerLayout == null) {
            return
        }
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, actionBarToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = find<NavigationView>(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this)
    }

    private fun isNavDrawerOpen(): Boolean = drawerLayout != null && drawerLayout?.isDrawerOpen(GravityCompat.START) ?: false

    private fun closeNavDrawer() = drawerLayout?.closeDrawer(GravityCompat.START)

    private fun setupAccount() {
        setAccountViews()
        /*if (Constants.studentModel == null) {
            val student = getPref(SharedPrefKeys.USER_KEY, "")
            if (student.isNullOrEmpty()) {
                getStudentInfo()
            } else {
                Constants.studentModel = JsonHelper.jsonToKt<Student>(student)
                setAccountViews(Constants.studentModel!!)
            }
        } else {
            setAccountViews(Constants.studentModel!!)
        }*/
    }

    private fun setAccountViews() {
        if (navigationView == null) return

        val header = navigationView!!.getHeaderView(0)
        val image = header.find<ImageView>(R.id.profile_image)
        val name = header.find<TextView>(R.id.profile_name_text)
        val role = header.find<TextView>(R.id.profile_role_text)

        /* val uri = getStudentProfileImageGlideUri(apiAccessToken)
         val glOpts = getStudentProfileImageGlideOptions()
         Glide.with(this).load(uri).apply(glOpts).into(image)*/

        name.text = "Madhusudhan Reddy N"
        role.text = "Branch Administrator"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == getSelfNavDrawerItem()) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            return true
        }

        handler.postDelayed({ goToNavViewItem(item.itemId) }, navDrawerLaunchDelay.toLong())

        // fade out the main content
        mainContent?.animate()?.alpha(0f)?.duration = mainContentFadeOutDuration.toLong()

        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    private fun goToNavViewItem(itemId: Int) {
        when (itemId) {
            R.id.nav_dashboard -> startActivity<DashboardActivity>()
        //R.id.nav_downloads -> startActivity<DownloadsActivity>()
        R.id.nav_contact -> startActivity<CameraActivity>()
        //R.id.nav_settings -> Log.d(TAG, "Settings clicked")
            R.id.nav_about -> startActivity<AboutActivity>()
            R.id.nav_logout -> startActivity<LogoutActivity>()
        }
        finishNoAnim()
    }

}