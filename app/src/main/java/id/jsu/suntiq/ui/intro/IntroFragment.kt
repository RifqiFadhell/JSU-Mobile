package id.jsu.suntiq.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import id.jsu.suntiq.R
import kotlinx.android.synthetic.main.intro_fragment.*

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initListener()
    }

    private fun init() {

    }

    private fun initListener() {
        buttonRegister.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_introFragment_to_registerFormFragment)
        }

        textGoLogin.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_introFragment_to_loginFragment)
        }
    }
}