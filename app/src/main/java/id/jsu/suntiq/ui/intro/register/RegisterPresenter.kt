package id.jsu.suntiq.ui.intro.register

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.register.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterPresenter(private val view: RegisterContract.View): RegisterContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun register(request: RegisterRequest) {
        disposable = apiFactory?.register(request)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getRegister(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }
}