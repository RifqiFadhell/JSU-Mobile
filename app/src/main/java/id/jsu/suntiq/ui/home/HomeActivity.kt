package id.jsu.suntiq.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import id.jsu.suntiq.R
import id.jsu.suntiq.ui.MainActivity
import id.jsu.suntiq.ui.home.dashboard.DashboardFragment
import id.jsu.suntiq.ui.home.delivery.finish.FinalConfirmationActivity
import id.jsu.suntiq.ui.home.duty.DutyLetterActivity
import id.jsu.suntiq.ui.home.history.HistoryFragment
import id.jsu.suntiq.ui.home.profile.ProfileFragment
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity() {

    private var dashboardFragment = DashboardFragment()
    private var historyFragment = HistoryFragment()
    private var profileFragment = ProfileFragment()
    private var fragmentManager = supportFragmentManager
    private var fragmentActive: Fragment = dashboardFragment

    private val LOCATION_PERMISSION = 42

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

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder(this)
                    .setTitle("Kamu belum meng-aktifkan gps kamu")  // GPS not found
                    .setMessage("Silahkan Nyalakan Gps kamu sebelum menggunakan maps") // Want to enable?
                    .setPositiveButton("Oke") { _, _ ->
                        this.startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    }
                    .setNegativeButton("Tidak") { _, _ -> onBackPressed() }
                    .setCancelable(false)
                    .show()
            }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                showLongToast("Berhasil mengizinkan lokasi")
            }
        }
    }

    private fun checkLogin() {
        if (getDataUser() == null) {
            goToActivity(MainActivity::class.java)
            finish()
        } else {
            if (getDataExistingProgress() != null && getDataExistingProgressDelivery() != null) {
                goToActivity(FinalConfirmationActivity::class.java)
                finish()
            } else if (getDataExistingProgress() != null) {
                goToActivity(DutyLetterActivity::class.java)
                finish()
            } else {
                checkPermission()
            }
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