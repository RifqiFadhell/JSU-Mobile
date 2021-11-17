package id.jsu.suntiq.ui.home.delivery

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationOfficeRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class DeliveryPresenter(private val view: DeliveryContract.View) :
    DeliveryContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun submitDataConfirmation(request: ConfirmationOfficeRequest) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("assignment_id", request.assignment_id.toString())
        builder.addFormDataPart("recipient_name", request.recipient_name.toString())
        builder.addFormDataPart("recipient_phone_number", request.recipient_phone_number.toString())
        builder.addFormDataPart("recipient_position", "123456789")
        builder.addFormDataPart("recipient_office_picture", request.recipient_office_picture?.name, request.recipient_office_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("recipient_picture", request.recipient_picture?.name, request.recipient_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_number_picture", request.vehicle_number_picture?.name, request.vehicle_number_picture!!.asRequestBody(MultipartBody.FORM))
        val requestBody: RequestBody = builder.build()

        disposable = apiFactory?.confirmDeliveryOffice(requestBody)
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