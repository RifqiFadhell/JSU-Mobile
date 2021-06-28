package id.jsu.suntiq.ui.intro.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.login.LoginRequest
import id.jsu.suntiq.api.response.login.LoginResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_PROFILE
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.HomeActivity
import id.jsu.suntiq.ui.intro.register.verify.VerifyRegisterActivity
import id.jsu.suntiq.utils.LoaderIndicatorHelper
import id.jsu.suntiq.utils.base.BaseFragment
import id.jsu.suntiq.utils.extensions.goToActivity
import id.jsu.suntiq.utils.extensions.showOkDialog
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment(), LoginContract.View {

    private var loading: LoaderIndicatorHelper? = null
    private var presenter: LoginPresenter? = null

    override fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun init(bundle: Bundle?) {
        loading = LoaderIndicatorHelper.getInstance()
        presenter = LoginPresenter(this)
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {
        buttonLogin.setOnClickListener { checkValidation() }
    }

    private fun checkValidation() {
        val number = editPhone.editableText.toString()
        val password = editPassword.editableText.toString()

        if (number.isEmpty()) {
            textInputPhone.error = "Nomor tidak boleh kosong"
        } else {
            textInputPhone.error = null
        }

        if (password.isEmpty()) {
            textInputPassword.error = "Password tidak boleh kosong"
        } else {
            textInputPassword.error = null
        }

        if (number.isNotEmpty() && password.isNotEmpty()) {
            presenter?.login(LoginRequest(number, password))
            showProgress()
        }
    }

    override fun getLogin(response: LoginResponse) {
        /*
        * 401 = inactive
        * 400 = no available
        * 402 = unverified
        * */
        when (response.status) {
            200 -> {
                TinyDB(requireContext()).putObject(TINY_PROFILE, response.data?.user)
                requireActivity().goToActivity(HomeActivity::class.java)
                requireActivity().finishAffinity()
            }
            401 -> {
                requireContext().showOkDialog("Akun anda belum aktif", "Oke", null)
            }
            402 -> {
                requireContext().showOkDialog(
                    "Akun anda belum terverifikasi , Silahkan Verifikasi Foto Wajah dan Ktp terlebih dahulu",
                    "Oke",
                    DialogInterface.OnClickListener { _, _ ->

                    }
                )
            }
        }
    }

    private fun gotoVerification(id: Int) {
        val intent = Intent(requireActivity(), VerifyRegisterActivity::class.java)

        requireActivity().startActivity(intent)
    }

    override fun showProgress() {
        loading?.showDialog(requireContext())
    }

    override fun hideProgress() {
        loading?.dismissDialog()
    }

    override fun showError(error: Throwable) {
        requireContext().showOkDialog(error.message.toString(), "Oke", null)
    }
}