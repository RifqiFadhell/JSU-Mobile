package id.jsu.suntiq.ui.home.cancel

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import id.jsu.suntiq.R
import id.jsu.suntiq.api.response.vehicle.cancel.CancelResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM_DELIVERY
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.HomeActivity
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import kotlinx.android.synthetic.main.cancel_activity.*
import kotlinx.android.synthetic.main.header.*

class CancelActivity : BaseActivity(), CancelContract.View {

    private var listCancellation = listOf("Debitur tidak kooperatif", "Lingkungan tidak kondusif", "Detail Kendaraan tidak cocok")
    private var adapter: CancelAdapter? = null
    private var presenter: CancelPresenter? = null
    private var cancelSelected: String = ""
    private var id: String? = ""

    override fun provideLayout() {
        setContentView(R.layout.cancel_activity)
    }

    override fun init(bundle: Bundle?) {
        id = getDataExistingProgress()?.id.toString()
        presenter =  CancelPresenter(this)
        adapter = CancelAdapter(listCancellation, this,
            onItemClick = { _: Int, item: String ->
                cancelSelected = item
                textOther.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_circle_gray), null, null, null)
                textInputOther.toGone()
        })
        listCancel.adapter = adapter
        listCancel.layoutManager = LinearLayoutManager(this)
        listCancel.itemAnimator = DefaultItemAnimator()
    }

    override fun initData(bundle: Bundle?) {
        textName.text = getDataUser()?.username
        textPhone.text = getDataUser()?.phone_number
    }

    override fun initListener(bundle: Bundle?) {
        textOther.setOnClickListener {
            setOther()
        }

        buttonNext.setOnClickListener {
            if (cancelSelected.isNotEmpty()) {
                showYesNoDialog("Yakin membatalkan proses ?", "Iya", "No", DialogInterface.OnClickListener { _, _ ->
                    cancelData()
                })
            } else {
                showOkDialog("Pilih salah satu alasan pembatalan", "Oke", null)
            }
        }

        buttonBack.setOnClickListener { onBackPressed() }
    }

    private fun setOther() {
        adapter?.selectTaskListItem(4)
        textInputOther.toVisible()
        textOther.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_check), null, null, null)
    }

    private fun cancelData() {
        val cancel = editOther.editableText.toString()
        val data: String = if (cancel.isNotEmpty()) {
            cancel
        } else {
            cancelSelected
        }
        presenter?.cancelData(id.orEmpty(), data)
        showLoading()
    }

    override fun getDataCancel(response: CancelResponse) {
        if (response.status == 200) {
            showOkDialog("Proses sukses di batalkan", "Oke", DialogInterface.OnClickListener { dialog, which ->
                TinyDB(this).remove(TINY_TEMPORARY_DATA_CONFIRM)
                TinyDB(this).remove(TINY_TEMPORARY_DATA_CONFIRM_DELIVERY)
                goToActivity(HomeActivity::class.java)
                finishAffinity()
            })
        }
    }

    override fun showLoading() {
        layoutLoading.toVisible()
    }

    override fun hideLoading() {
        layoutLoading.toGone()
    }

    override fun showError(error: Throwable) {
        showOkDialog(error.message.orEmpty(), "Oke", null)
    }
}