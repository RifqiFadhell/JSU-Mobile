package id.jsu.suntiq.ui.home.confirmation

import id.jsu.suntiq.api.request.location.LocationsRequest
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationRequest
import id.jsu.suntiq.api.response.location.LocationResponse
import id.jsu.suntiq.api.response.vehicle.confirmation.ConfirmationVehicleResponse

class ConfirmationVehicleContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showError(throwable: Throwable)

        fun getListLocation(response: LocationResponse)

        fun getDataConfirmation(response: ConfirmationVehicleResponse)
    }

    interface Presenter {
        fun listLocation(request: LocationsRequest)

        fun submitDataConfirmation(request: ConfirmationRequest)
    }
}