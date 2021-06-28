package id.jsu.suntiq.ui.intro.register.verify

import id.jsu.suntiq.CustomApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class VerifyPresenter(private val view: VerifyContract.View): VerifyContract.Presenter {

    private var apiFactory = CustomApplication.apiClient
    private var disposable: Disposable? = null

    override fun verify(selfie: File, ktp: File, id:String) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("user_id", id)
        builder.addFormDataPart("selfie", selfie.name, selfie.asRequestBody(MultipartBody.FORM))
        builder.addFormDataPart("ktp", ktp.name, ktp.asRequestBody(MultipartBody.FORM))
        val requestBody: RequestBody = builder.build()

        disposable = apiFactory?.verify(requestBody)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                view.getVerify(result)
                view.hideLoading()
            }, {
                view.showError(it)
                view.hideLoading()
            })
    }
}