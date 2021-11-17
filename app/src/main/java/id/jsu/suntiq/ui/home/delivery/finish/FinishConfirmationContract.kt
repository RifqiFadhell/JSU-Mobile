package id.jsu.suntiq.ui.home.delivery.finish

import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationOfficeRequest
import id.jsu.suntiq.api.request.vehicle.confirmation.FinishConfirmationRequest
import id.jsu.suntiq.api.response.vehicle.delivery.ConfirmationDeliveryResponse
import id.jsu.suntiq.api.response.vehicle.finish.FinishConfirmationResponse
import java.util.*

class FinishConfirmationContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showError(throwable: Throwable)

        fun getDataConfirmation(response: FinishConfirmationResponse)
    }

    interface Presenter {
        fun submitDataConfirmation(request: FinishConfirmationRequest)
    }
}