package id.jsu.suntiq.ui.home.delivery.finish

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationOfficeRequest
import id.jsu.suntiq.api.request.vehicle.confirmation.FinishConfirmationRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class FinishConfirmationPresenter(private val view: FinishConfirmationContract.View) :
    FinishConfirmationContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun submitDataConfirmation(request: FinishConfirmationRequest) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("assignment_id", request.assignment_id.toString())
        builder.addFormDataPart("serah_terima_picture", request.serah_terima_picture?.name, request.serah_terima_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_side_a_picture", request.vehicle_side_a_picture?.name, request.vehicle_side_a_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_side_b_picture", request.vehicle_side_b_picture?.name, request.vehicle_side_b_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_side_c_picture", request.vehicle_side_c_picture?.name, request.vehicle_side_c_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("vehicle_side_d_picture", request.vehicle_side_d_picture?.name, request.vehicle_side_d_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("tanda_tangan_bastk_picture", request.tanda_tangan_bastk_picture?.name, request.tanda_tangan_bastk_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("bastk_picture", request.bastk_picture?.name, request.bastk_picture!!.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("group_picture", request.group_picture?.name, request.group_picture!!.asRequestBody(MultipartBody.FORM))
        val requestBody: RequestBody = builder.build()

        disposable = apiFactory?.confirmFinishAssignment(requestBody)
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