package id.jsu.suntiq.ui.home.dashboard

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.vehicle.DetailRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DashboardPresenter(private val view: DashboardContract.View): DashboardContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun allData() {
        disposable = apiFactory?.getVehicle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getAllData(result)
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }

    override fun detailVehicle(id: String) {
        disposable = apiFactory?.detailVehicle(DetailRequest(id))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getDetailVehicle(result)
                view.hideProgress()
            }, {
                view.showError(it)
                view.hideProgress()
            })
    }
}