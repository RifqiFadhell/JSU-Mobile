package id.jsu.suntiq.utils.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideLayout()
        init(savedInstanceState)
        initData(savedInstanceState)
        initListener(savedInstanceState)
    }

    protected abstract fun provideLayout()

    protected abstract fun init(bundle: Bundle?)

    protected abstract fun initData(bundle: Bundle?)

    protected abstract fun initListener(bundle: Bundle?)
}