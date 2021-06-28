package id.jsu.suntiq.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.jsu.suntiq.R
import id.jsu.suntiq.utils.base.BaseFragment

class ProfileFragment : BaseFragment() {

    override fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun init(bundle: Bundle?) {

    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {

    }
}