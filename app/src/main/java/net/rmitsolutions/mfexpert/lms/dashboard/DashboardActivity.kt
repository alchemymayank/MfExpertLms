package net.rmitsolutions.mfexpert.lms.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.apiAccessToken
import net.rmitsolutions.mfexpert.lms.helpers.hideProgress
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.helpers.showProgress
import net.rmitsolutions.mfexpert.lms.network.IMasters
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    @Inject
    lateinit var mastersService: IMasters

    private lateinit var relationsList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        relationsList = findViewById(R.id.relationsList)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()

        depComponent.injectDashboardActivity(this)
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard

    fun getRelations(view: View) {
        showProgress()
        compositeDisposable.add(mastersService.getRelations(apiAccessToken)
                .processRequest(
                        { relations ->
                            hideProgress()
                            var rel = ""
                            relations.forEach { r -> rel += r.name + " == " }
                            relationsList.text = rel
                        },
                        { err ->
                            hideProgress(true, err)
                        }
                )

        )
    }


}
