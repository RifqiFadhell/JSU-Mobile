package id.jsu.suntiq.ui.home.cancel

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.vehicle.cancel.CancelRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CancelPresenter(val view: CancelContract.View): CancelContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun cancelData(id: String, reason: String) {
        disposable = apiFactory?.cancelAssignment(CancelRequest(id, reason))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getDataCancel(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }
}