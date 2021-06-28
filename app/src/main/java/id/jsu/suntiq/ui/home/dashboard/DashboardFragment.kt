package id.jsu.suntiq.ui.home.dashboard

import android.content.DialogInterface
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
import id.jsu.suntiq.api.response.vehicle.DetailVehicle
import id.jsu.suntiq.api.response.vehicle.DetailVehicleResponse
import id.jsu.suntiq.api.response.vehicle.VehicleResponse
import id.jsu.suntiq.preference.room.DataDatabase
import id.jsu.suntiq.preference.room.DataEntity
import id.jsu.suntiq.utils.CheckConnectionUtils
import id.jsu.suntiq.utils.ImageUtils
import id.jsu.suntiq.utils.LoaderIndicatorHelper
import id.jsu.suntiq.utils.NavigationBarUtil
import id.jsu.suntiq.utils.base.BaseFragment
import id.jsu.suntiq.utils.extensions.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.*
import kotlinx.android.synthetic.main.detail_data_bottom_sheet.view.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.image
import kotlinx.android.synthetic.main.login_fragment.*

class DashboardFragment : BaseFragment(), DashboardContract.View {

    private var loading: LoaderIndicatorHelper? = null
    private var presenter: DashboardPresenter? = null
    private var adapter: VehicleOfflineAdapter? = null
    private var listVehicle = ArrayList<DataEntity>()
    private val compositeDisposable = CompositeDisposable()
    private var dataDatabase: DataDatabase? = null

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
        initBottomDialog()
        presenter = DashboardPresenter(this)
        dataDatabase = DataDatabase.getInstance(requireContext())
        checkData()
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
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {
        buttonSearch.setOnClickListener { search() }
        editSearch.setOnEditorActionListener { v, actionId, event ->
            search()
            view?.let { requireContext().hideKeyboard(it) }
            false
        }
        bottomDialog.buttonClose.setOnClickListener { bottomDialog.dismiss() }
        bottomDialog.checkYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bottomDialog.checkNo.isChecked = false
            }
        }
        bottomDialog.checkNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bottomDialog.checkYes.isChecked = false
            }
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
            presenter?.detailVehicle(data.id.toString())
            showProgress()
        } else {
            requireContext().showOkDialog("Anda harus online jika ingin memproses data", "Oke", null)
        }
    }

    private fun checkData() {
        compositeDisposable.add(
            dataDatabase!!.dataDao().checkData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isNullOrEmpty()) {
                        presenter?.allData()
                        showLoading()
                    }
                }
        )
    }

    private fun search() {
        val search = editSearch.editableText.toString()
        compositeDisposable.add(
            dataDatabase!!.dataDao().searchData("%$search%")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!it.isNullOrEmpty()) {
                        groupSearch.toGone()
                        textResult.text =
                            getString(R.string.home_result, search, it.size.toString())
                        listVehicle.clear()
                        listVehicle.addAll(it)
                        adapter?.notifyDataSetChanged()
                    } else {
                        groupSearch.toVisible()
                    }
                }
        )
    }

    override fun getAllData(response: VehicleResponse) {
        if (response.status == 200) {
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
                    .subscribe {
                        hideLoading()
                    })
            }
        }
    }

    override fun getDetailVehicle(response: DetailVehicleResponse) {
        if (response.status == 200) {
            response.data?.let { setDetailVehicle(it) }
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
        bottomDialog.textLeasing.text = data.leasing
        bottomDialog.textCasing.text = data.chassingNumber
        bottomDialog.textEngine.text = data.machineNumber
        bottomDialog.show()
    }

    override fun showError(throwable: Throwable) {
        requireActivity().showOkDialog(throwable.message.toString(), "Oke", null)
    }

    override fun showProgress() {
        loading?.showDialog(requireContext())
    }

    override fun hideProgress() {
        loading?.dismissDialog()
    }

    override fun showLoading() {
        layoutLoading.toVisible()
    }

    override fun hideLoading() {
        layoutLoading.toGone()
    }
}