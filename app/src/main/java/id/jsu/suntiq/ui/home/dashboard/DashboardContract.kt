package id.jsu.suntiq.ui.home.dashboard

import id.jsu.suntiq.api.response.CompleteResponse
import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.StatusUpdateResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse

class DashboardContract {

    interface View {
        fun showLoading()

        fun hideLoading()

        fun showProgress()

        fun hideProgress()

        fun getAllData(response: VehicleResponse)

        fun getDetailVehicle(response: DetailVehicleResponse)

        fun getStatusUpdate(response: StatusUpdateResponse)

        fun getCompleteData(response: CompleteResponse)

        fun showError(throwable: Throwable)
    }

    interface Presenter {
        fun allData(size:Int, page: Int)

        fun detailVehicle(id: String)

        fun getUpdateStatus()

        fun sendCompleteData()
    }
}