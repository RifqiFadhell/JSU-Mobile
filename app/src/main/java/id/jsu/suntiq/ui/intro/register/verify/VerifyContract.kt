package id.jsu.suntiq.ui.intro.register.verify

import id.jsu.suntiq.api.response.register.verify.VerifyResponse
import java.io.File

class VerifyContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showError(throwable: Throwable)

        fun getVerify(response: VerifyResponse)
    }
    interface Presenter {

        fun verify(selfie: File, ktp:File, id:String)
    }
}