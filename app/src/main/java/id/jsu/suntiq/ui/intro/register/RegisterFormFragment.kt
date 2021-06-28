package id.jsu.suntiq.ui.intro.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.register.RegisterRequest
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_FORM
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_IMEI
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.utils.base.BaseFragment
import kotlinx.android.synthetic.main.register_form_fragment.*
import kotlinx.android.synthetic.main.register_form_fragment.textInputPassword
import kotlinx.android.synthetic.main.register_form_fragment.textInputPhone

class RegisterFormFragment : BaseFragment() {

    override fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.register_form_fragment, container, false)
    }

    override fun init(bundle: Bundle?) {
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initListener(bundle: Bundle?) {
        buttonRegister.setOnClickListener {
            validateForm()
        }
    }

    private fun validateForm() {
        val name = editName.editableText.toString()
        val number = editPhone.editableText.toString()
        val password = editPassword.editableText.toString()
        val repassword = editRepeatPassword.editableText.toString()
        val imei = TinyDB(requireContext()).getString(TINY_IMEI)

        if (number.isEmpty()) {
            textInputPhone.error = "Nomor tidak boleh kosong"
        } else {
            textInputPhone.error = null
        }

        when {
            password.isEmpty() -> {
                textInputPassword.error = "Password tidak boleh kosong"
            }
            password.length < 6 -> {
                textInputPassword.error = "Password minimal 6"
            }
            else -> {
                textInputPassword.error = null
            }
        }

        if (name.isEmpty()) {
            textInputName.error = "Nama tidak boleh kosong"
        } else {
            textInputName.error = null
        }

        if (repassword != password) {
            textInputRepeatPassword.error = "Password Harus sama"
        } else {
            textInputRepeatPassword.error = null
        }

        if (name.isNotEmpty() && number.isNotEmpty() && password.isNotEmpty() && textInputRepeatPassword.error == null) {
            nextStep()
            TinyDB(requireContext()).putObject(TINY_FORM, RegisterRequest(number, password, name, "", "", "", "", "", imei))
        }
    }

    private fun nextStep() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_registerFormFragment_to_completeRegisterFragment)
    }
}