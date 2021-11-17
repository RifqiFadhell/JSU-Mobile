package id.jsu.suntiq.ui.home.delivery

import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationOfficeRequest
import id.jsu.suntiq.api.response.vehicle.delivery.ConfirmationDeliveryResponse
import java.util.*

class DeliveryContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showError(throwable: Throwable)

        fun getDataConfirmation(response: ConfirmationDeliveryResponse)
    }

    interface Presenter {
        fun submitDataConfirmation(request: ConfirmationOfficeRequest)
    }
}