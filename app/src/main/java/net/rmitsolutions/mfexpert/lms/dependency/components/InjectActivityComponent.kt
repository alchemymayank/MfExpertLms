package net.rmitsolutions.mfexpert.lms.dependency.components

import dagger.Component
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.dashboard.DashboardActivity
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ActivityScope

/**
 * Created by Madhu on 24-Apr-2018.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectActivityComponent {
    fun injectBaseActivity(baseActivity: BaseActivity)
    fun injectDashboardActivity(activity: DashboardActivity)

}