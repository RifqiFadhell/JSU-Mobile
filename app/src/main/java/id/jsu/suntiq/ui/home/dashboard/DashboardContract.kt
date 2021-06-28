package id.jsu.suntiq.ui.home.dashboard

import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse

class DashboardContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showProgress()

        fun hideProgress()

        fun getAllData(response: VehicleResponse)

        fun getDetailVehicle(response: DetailVehicleResponse)

        fun showError(throwable: Throwable)
    }

    interface Presenter {
        fun allData()

        fun detailVehicle(id: String)
    }
}