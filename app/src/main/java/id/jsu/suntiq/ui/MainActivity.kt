package id.jsu.suntiq.ui

import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import id.jsu.suntiq.R
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_IMEI
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.utils.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun provideLayout() {
        setContentView(R.layout.main_activity)
    }

    override fun init(bundle: Bundle?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.introFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        findViewById<Toolbar>(R.id.toolbarMain)
            .setupWithNavController(navController, appBarConfiguration)

        TinyDB(this).putString(TINY_IMEI, Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID))
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {

    }
}