package id.jsu.suntiq.ui.intro.register

import id.jsu.suntiq.api.request.register.RegisterRequest
import id.jsu.suntiq.api.response.register.RegisterResponse

class RegisterContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun getRegister(response: RegisterResponse)

        fun showError(error: Throwable)
    }

    interface Presenter {
        fun register(request: RegisterRequest)
    }
}