package id.jsu.suntiq.ui.home.cancel

import id.jsu.suntiq.api.response.vehicle.cancel.CancelResponse

class CancelContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun getDataCancel(response: CancelResponse)
        fun showError(error: Throwable)
    }

    interface Presenter {
        fun cancelData(id: String, reason: String)
    }
}