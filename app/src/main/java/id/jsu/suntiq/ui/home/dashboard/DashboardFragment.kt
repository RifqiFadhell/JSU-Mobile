package id.jsu.suntiq.ui.home.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.jsu.suntiq.R
import id.jsu.suntiq.api.response.CompleteResponse
import id.jsu.suntiq.api.response.login.User
import id.jsu.suntiq.api.response.vehicle.DetailVehicle
import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.StatusUpdateResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse
import id.jsu.suntiq.preference.room.DataDatabase
import id.jsu.suntiq.preference.room.DataEntity
import id.jsu.suntiq.preference.tinyDb.TinyConstant
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_DATA_SUCCESS
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_PROFILE
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.MainActivity
import id.jsu.suntiq.ui.home.confirmation.ConfirmationVehicleActivity
import id.jsu.suntiq.utils.CheckConnectionUtils
import id.jsu.suntiq.utils.NavigationBarUtil
import id.jsu.suntiq.utils.base.BaseFragment
import id.jsu.suntiq.utils.extensions.getActionBarHeight
import id.jsu.suntiq.utils.extensions.getScreenHeight
import id.jsu.suntiq.utils.extensions.getToken
import id.jsu.suntiq.utils.extensions.goToActivity
import id.jsu.suntiq.utils.extensions.hideKeyboard
import id.jsu.suntiq.utils.extensions.showOkDialog
import id.jsu.suntiq.utils.extensions.showYesNoDialog
import id.jsu.suntiq.utils.extensions.toGone
import id.jsu.suntiq.utils.extensions.toVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.buttonClose
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.buttonSubmit
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.checkNo
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.checkYes
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.imageDialog
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textCasing
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textColor
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textEngine
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textLeasing
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textNameVehicle
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textNumberPlate
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textType
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.textYear
import kotlinx.android.synthetic.main.home_fragment.buttonLogout
import kotlinx.android.synthetic.main.home_fragment.buttonSearch
import kotlinx.android.synthetic.main.home_fragment.editSearch
import kotlinx.android.synthetic.main.home_fragment.groupSearch
import kotlinx.android.synthetic.main.home_fragment.layoutLoading
import kotlinx.android.synthetic.main.home_fragment.layoutLoadingSync
import kotlinx.android.synthetic.main.home_fragment.list
import kotlinx.android.synthetic.main.home_fragment.textResult
import kotlinx.android.synthetic.main.home_fragment.textTotalData
import kotlinx.android.synthetic.main.home_fragment.textWelcome


class DashboardFragment : BaseFragment(), DashboardContract.View {

    private var presenter: DashboardPresenter? = null
    private var adapter: VehicleOfflineAdapter? = null
    private var listVehicle = ArrayList<DataEntity>()
    private val compositeDisposable = CompositeDisposable()
    private var dataDatabase: DataDatabase? = null
    private var detail: DetailVehicle? = null
    private var tinyDB: TinyDB? = null

    private var isCheckFrom: Boolean? = null
    private var token: String? = ""

    private lateinit var bottomDialog: BottomSheetDialog
    lateinit var viewDialog: View

    override fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun init(bundle: Bundle?) {
        token = requireContext().getToken()
        initBottomDialog()
        textResult.toGone()
        tinyDB = TinyDB(requireContext())
        presenter = DashboardPresenter(this)
        dataDatabase = DataDatabase.getInstance(requireContext())
        adapter = VehicleOfflineAdapter(listVehicle, requireContext(), object : OnItemClickOffline {
            override fun itemClicked(position: Int, view: View, vehicle: DataEntity) {
                requireContext().showYesNoDialog(
                    getString(
                        R.string.home_template_dialog,
                        vehicle.policeNumber
                    ), "Oke", "Tidak", DialogInterface.OnClickListener { _, _ ->
                        validateConnection(vehicle)
                    })
            }
        })
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext())
        list.itemAnimator = DefaultItemAnimator()
        presenter?.getUpdateStatus()
    }

    override fun initData(bundle: Bundle?) {
        textWelcome.text = getString(
            R.string.home_title,
            tinyDB?.getObject(TINY_PROFILE, User::class.java)?.username.toString()
        )
        compositeDisposable.add(
            dataDatabase!!.dataDao().getAllData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    textTotalData.text = "Total Data Kendaraan: ${it.size}"
                }
        )
        if (!TinyDB(requireContext()).getString(TINY_DATA_SUCCESS).isNullOrEmpty()) {
            compositeDisposable.add(Observable.fromCallable {
                dataDatabase?.dataDao()?.deleteData("%${TinyDB(requireContext()).getString(TINY_DATA_SUCCESS)}%")
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    TinyDB(requireContext()).remove(TINY_DATA_SUCCESS)
                })
        }
    }

    override fun initListener(bundle: Bundle?) {
        buttonSearch.setOnClickListener { search() }
        editSearch.setOnEditorActionListener { v, actionId, event ->
            search()
            false
        }
        bottomDialog.buttonClose.setOnClickListener { bottomDialog.dismiss() }
        bottomDialog.checkYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bottomDialog.checkNo.isChecked = false
                isCheckFrom = true
            }
        }
        bottomDialog.checkNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bottomDialog.checkYes.isChecked = false
                isCheckFrom = false
            }
        }

        bottomDialog.buttonSubmit.setOnClickListener {
            when (isCheckFrom) {
                true -> {
                    val intent = Intent(requireActivity(), ConfirmationVehicleActivity::class.java)
                    intent.putExtra("detail", detail)
                    intent.putExtra("from_jsu", isCheckFrom!!)
                    requireActivity().startActivity(intent)
                }
                false -> {
                    bottomDialog.dismiss()
                }
                else -> {
                    requireActivity().showOkDialog("Wajib Ceklis 'Ya' atau 'Tidak'", "Oke", null)
                }
            }
        }

        buttonLogout.setOnClickListener {
            tinyDB?.remove(TINY_PROFILE)
            requireActivity().goToActivity(MainActivity::class.java)
            requireActivity().finishAffinity()
        }
    }

    private fun initBottomDialog() {
        bottomDialog = BottomSheetDialog(requireContext())
        viewDialog = layoutInflater.inflate(R.layout.detail_data_bottom_sheet, null)
        bottomDialog.setContentView(viewDialog)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            NavigationBarUtil.setWhiteNavigationBar(requireActivity(), bottomDialog)
        }

        val params = (viewDialog.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        params.height =
            requireActivity().getScreenHeight() - (requireActivity().getActionBarHeight() * 2)

        val behavior = params.behavior
        (behavior as BottomSheetBehavior<*>).peekHeight =
            requireActivity().getScreenHeight() - (requireActivity().getActionBarHeight() * 2)
        bottomDialog.currentFocus
    }

    private fun validateConnection(data: DataEntity) {
        if (CheckConnectionUtils.isOnline(requireContext())) {
            presenter?.detailVehicle(data.idVehicle)
            showProgress()
        } else {
            requireContext().showOkDialog(
                "Anda harus online jika ingin memproses data",
                "Oke",
                null
            )
        }
    }

    private fun search() {
        view?.let { requireContext().hideKeyboard(it) }
        val search = editSearch.editableText.toString()
        compositeDisposable.add(
            dataDatabase!!.dataDao().searchData("%$search%")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!it.isNullOrEmpty()) {
                        list.toVisible()
                        groupSearch.toGone()
                        textResult.toVisible()
                        textResult.text =
                            getString(R.string.home_result, it.size.toString(), search)
                        listVehicle.clear()
                        listVehicle.addAll(it)
                        adapter?.notifyDataSetChanged()
                    } else {
                        list.toGone()
                        groupSearch.toVisible()
                        textResult.text = "Kami tidak dapat menemukan data hasil dari $search"
                        textResult.toVisible()
                    }
                }
        )
    }

    private fun checkData() {
        compositeDisposable.add(
            dataDatabase!!.dataDao().checkData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isNullOrEmpty()) {
                        presenter?.allData(5000, 1)
                        showLoading()
                    }
                }
        )
    }

    override fun getAllData(response: VehicleResponse) {
        if (!response.data.isNullOrEmpty()) {
            val list = ArrayList<DataEntity>()
            val listData = response.data
            for (x in listData.indices) {
                list.add(
                    DataEntity(
                        listData[x].policeNumber.toString(),
                        listData[x].id.toString(),
                        listData[x].type.toString(),
                        listData[x].category.toString(),
                        listData[x].chassingNumber.toString()
                    )
                )
            }
            compositeDisposable.add(Observable.fromCallable {
                dataDatabase?.dataDao()?.insertData(list)
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {})
        }
    }

    override fun getDetailVehicle(response: DetailVehicleResponse) {
        if (response.status == 200) {
            response.data?.let { setDetailVehicle(it) }
        } else {
            requireContext().showOkDialog(response.message.orEmpty(), "Oke", null)
        }
    }

    private fun setDetailVehicle(data: DetailVehicle) {
        if (data.category == "R2") {
            bottomDialog.imageDialog.setImageResource(R.drawable.ic_motorcycle)
        } else {
            bottomDialog.imageDialog.setImageResource(R.drawable.ic_car)
        }
        bottomDialog.textNumberPlate.text = data.policeNumber
        bottomDialog.textNameVehicle.text = data.type
        bottomDialog.textType.text = data.type
        bottomDialog.textColor.text = data.color
        bottomDialog.textYear.text = ""
        bottomDialog.textLeasing.text = data.leasing?.leasingName
        bottomDialog.textCasing.text = data.chassingNumber
        bottomDialog.textEngine.text = data.machineNumber
        bottomDialog.show()
        detail = data
    }

    override fun getStatusUpdate(response: StatusUpdateResponse) {
        if (response.data?.syncStatus.orEmpty() != "complete") {
            presenter?.allData(5000, 0)
            compositeDisposable.add(Observable.fromCallable {
                dataDatabase?.dataDao()?.deleteAll()
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {})
        } else {
            checkData()
        }
    }

    override fun getCompleteData(response: CompleteResponse) {

    }

    override fun showError(throwable: Throwable) {
        requireActivity().showOkDialog(throwable.message.toString(), "Oke", null)
    }

    override fun showProgress() {
        layoutLoading.toVisible()
    }

    override fun hideProgress() {
        layoutLoading.toGone()
    }

    override fun showLoading() {
        layoutLoadingSync.toVisible()
    }

    override fun hideLoading() {
        layoutLoadingSync.toGone()
    }
}