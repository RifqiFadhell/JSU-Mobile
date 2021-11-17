package id.jsu.suntiq.ui.home.confirmation

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.location.LocationsRequest
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class ConfirmationVehiclePresenter(private val view: ConfirmationVehicleContract.View) :
    ConfirmationVehicleContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun listLocation(request: LocationsRequest) {
        disposable = apiFactory?.listLocation(request)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getListLocation(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }

    override fun submitDataConfirmation(request: ConfirmationRequest) {
        val fromJsu = when (request.from_jsu) {
            true -> 1
            false -> 0
            else -> 0
        }
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("vehicle_id", request.vehicle_id.toString())
        builder.addFormDataPart("receiving_office_id", request.receiving_office_id.toString())
        builder.addFormDataPart("from_jsu", fromJsu.toString())
        builder.addFormDataPart("stnk", "123456789")
        builder.addFormDataPart("vehicle_side_a_picture", request.vehicle_side_a_picture?.name, request.vehicle_side_a_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_side_b_picture", request.vehicle_side_b_picture?.name, request.vehicle_side_b_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_registration_picture", request.vehicle_registration_picture?.name, request.vehicle_side_b_picture!!.asRequestBody(MultipartBody.FORM))
        val requestBody: RequestBody = builder.build()

        disposable = apiFactory?.confirmVehicle(requestBody)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getDataConfirmation(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }
}