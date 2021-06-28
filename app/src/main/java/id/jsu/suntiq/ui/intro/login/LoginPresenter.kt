package id.jsu.suntiq.ui.intro.login

import id.jsu.suntiq.CustomApplication
import id.jsu.suntiq.api.request.login.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private var view: LoginContract.View): LoginContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun login(request: LoginRequest) {
        disposable = apiFactory?.login(request)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getLogin(result)
                view.hideProgress()
            }, {
                view.showError(it)
                view.hideProgress()
            })
    }
}