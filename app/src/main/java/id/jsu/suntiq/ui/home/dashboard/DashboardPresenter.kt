package id.jsu.suntiq.ui.home.dashboard

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.CompleteRequest
import id.jsu.suntiq.api.request.vehicle.detail.DetailRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null
    private var totalPage: Int = 0

    override fun allData(size: Int, page: Int) {
        view.showLoading()
        disposable = apiFactory?.getVehicle(size, page)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getAllData(result)
                if (totalPage == 0) {
                    totalPage = result.lastPage?:0
                }
                validateData(page)
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }

    private fun validateData(page: Int) {
        if (page != totalPage) {
            allData(5000, page + 1)
        } else {
            sendCompleteData()
        }
    }

    override fun detailVehicle(id: String) {
        view.showProgress()
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

    override fun getUpdateStatus() {
        view.showLoading()
        disposable = apiFactory?.getStatusUpdate()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getStatusUpdate(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }

    override fun sendCompleteData() {
        disposable = apiFactory?.completeUpdateData(CompleteRequest())
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getCompleteData(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }
}