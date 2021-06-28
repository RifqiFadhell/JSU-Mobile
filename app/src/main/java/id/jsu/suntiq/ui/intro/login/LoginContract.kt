package id.jsu.suntiq.ui.intro.login

import id.jsu.suntiq.api.response.login.LoginResponse
import id.jsu.suntiq.api.request.login.LoginRequest

class LoginContract {

    interface View {

        fun showProgress()

        fun hideProgress()

        fun showError(error: Throwable)

        fun getLogin(response: LoginResponse)
    }

    interface Presenter {

        fun login(request: LoginRequest)
    }
}