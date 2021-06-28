package id.jsu.suntiq.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import id.jsu.suntiq.R
import id.jsu.suntiq.ui.MainActivity
import id.jsu.suntiq.ui.home.dashboard.DashboardFragment
import id.jsu.suntiq.ui.home.history.HistoryFragment
import id.jsu.suntiq.ui.home.profile.ProfileFragment
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.getDataUser
import id.jsu.suntiq.utils.extensions.goToActivity
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity() {

    private var dashboardFragment = DashboardFragment()
    private var historyFragment = HistoryFragment()
    private var profileFragment = ProfileFragment()
    private var fragmentManager = supportFragmentManager
    private var fragmentActive: Fragment = dashboardFragment

    override fun provideLayout() {
        setContentView(R.layout.home_activity)
    }

    override fun init(bundle: Bundle?) {
        checkLogin()
        initFragment()
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeDashboard -> {
                    fragmentManager.beginTransaction().hide(fragmentActive).show(dashboardFragment)
                        .commit()
                    fragmentActive = dashboardFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.homeHistory -> {
                    fragmentManager.beginTransaction().hide(fragmentActive).show(historyFragment)
                        .commit()
                    fragmentActive = historyFragment
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.homeProfile -> {
                    fragmentManager.beginTransaction().hide(fragmentActive).show(profileFragment)
                        .commit()
                    fragmentActive = profileFragment
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun checkLogin() {
        if (getDataUser() == null) {
            goToActivity(MainActivity::class.java)
            finish()
        }
    }

    private fun initFragment() {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.frameFragment, dashboardFragment)
        transaction.add(R.id.frameFragment, historyFragment).hide(historyFragment)
        transaction.add(R.id.frameFragment, profileFragment).hide(profileFragment)
        transaction.commit()
    }
}