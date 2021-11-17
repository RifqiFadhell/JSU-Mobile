package id.jsu.suntiq.ui.home.confirmation

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationRequest
import id.jsu.suntiq.api.response.location.LocationItem
import id.jsu.suntiq.api.response.location.LocationResponse
import id.jsu.suntiq.api.response.vehicle.DetailVehicle
import id.jsu.suntiq.api.response.vehicle.confirmation.ConfirmationVehicleResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_LOCATION
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.duty.DutyLetterActivity
import id.jsu.suntiq.ui.home.map.MapActivity
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.confirmation_vehicle_activity.*
import kotlinx.android.synthetic.main.header.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File
import java.util.*
import kotlinx.coroutines.launch

class ConfirmationVehicleActivity : BaseActivity(), ConfirmationVehicleContract.View {

    companion object {
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
        private const val CAMERA_REQUEST_CODE = 7500
    }

    private var easyImage: EasyImage? = null
    private var unit1: ArrayList<MediaFile> = ArrayList()
    private var unit2: ArrayList<MediaFile> = ArrayList()
    private var idPhoto: ArrayList<MediaFile> = ArrayList()

    private var unit1File: File? = null
    private var unit2File: File? = null
    private var idFile: File? = null

    private var code: Int? = 0
    private var fromJsu: Boolean? = null
    private var token: String? = ""

    private var dataLocation: LocationItem? = null
    private var dataVehicle: DetailVehicle? = null

    private var presenter: ConfirmationVehiclePresenter? = null

    private var tinyDb: TinyDB? = null

    override fun provideLayout() {
        setContentView(R.layout.confirmation_vehicle_activity)
    }

    override fun init(bundle: Bundle?) {
        if (intent != null) {
            dataVehicle = intent.getSerializableExtra("detail") as DetailVehicle?
            fromJsu = intent.getBooleanExtra("from_jsu", false)
            setDetailVehicle()
        }
        token = getToken()
        presenter = ConfirmationVehiclePresenter(this)
        tinyDb = TinyDB(this)
        tinyDb?.remove(TINY_LOCATION)
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initListener(bundle: Bundle?) {
        imageUnit1.setOnClickListener {
            openCamera()
            code = 1
        }
        imageUnit2.setOnClickListener {
            openCamera()
            code = 2
        }
        imageId.setOnClickListener {
            openCamera()
            code = 3
        }

        buttonSubmit.setOnClickListener {
            validate()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }

        editLocation.setOnClickListener {
            goToActivity(MapActivity::class.java)
        }
    }

    private fun validate() {
        if (unit1File != null && unit2File != null && idFile != null && dataLocation != null) {
            showYesNoDialog(
                "Apakah anda sudah di Lokasi Cabang ?\njika sudah silahkan lanjutkan Proses...",
                "Oke",
                "Belum",
                DialogInterface.OnClickListener { _, _ ->
                    presenter?.submitDataConfirmation(ConfirmationRequest(
                        getDataUser()?.id.toString(), fromJsu, dataVehicle?.id.toString(),
                        dataLocation?.id.toString(), unit1File, unit2File, idFile))
                    showLoading()
                })
        } else {
            showOkDialog("Foto wajib di cantumkan semua & lokasi wajib di pilih", "Oke", null)
        }
    }

    private fun saveData(response: ConfirmationVehicleResponse) {
        val data = response.data?.docs?.suratJalan
        val intent = Intent(this, DutyLetterActivity::class.java)
        intent.putExtra("docs", data)
        tinyDb?.putObject(TINY_TEMPORARY_DATA_CONFIRM, response.data)
        startActivity(intent)
    }

    private fun setDetailVehicle() {
        textNumber.text = dataVehicle?.policeNumber
        textMark.text = dataVehicle?.chassingNumber
        textType.text = dataVehicle?.type
        textColor.text = dataVehicle?.color
        textYear.text = "-"
        textLeasing.text = dataVehicle?.leasing
        textName.text = getDataUser()?.username
        textPhone.text = getDataUser()?.phone_number
    }

    override fun getListLocation(response: LocationResponse) {}

    override fun getDataConfirmation(response: ConfirmationVehicleResponse) {
        if (response.status == 200) {
            saveData(response)
        }
    }

    private fun openCamera() {
        easyImage = EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(false)
            .setFolderName("Jsu")
            .allowMultiple(false).build()
        val necessaryPermissions = arrayOf(Manifest.permission.CAMERA)
        if (arePermissionsGranted(necessaryPermissions)) {
            easyImage?.openCameraForImage(this)
        } else {
            requestPermissionsCompat(
                necessaryPermissions,
                CAMERA_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage?.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(
                    imageFiles: Array<MediaFile>,
                    source: MediaSource
                ) {
                    when (code) {
                        1 -> {
                            onPhotosUnit1(imageFiles)
                        }
                        2 -> {
                            onPhotosUnit2(imageFiles)
                        }
                        3 -> {
                            onPhotosId(imageFiles)
                        }
                    }
                }
            })
    }

    private fun onPhotosUnit1(returnedPhotos: Array<MediaFile>) {
        unit1.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit1File = file?.let { Compressor.compress(this@ConfirmationVehicleActivity, it) }
            unit1File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUnit1)
            }
        }
    }

    private fun onPhotosUnit2(returnedPhotos: Array<MediaFile>) {
        unit2.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit2File = file?.let { Compressor.compress(this@ConfirmationVehicleActivity, it) }
            unit2File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUnit2)
            }
        }
    }

    private fun onPhotosId(returnedPhotos: Array<MediaFile>) {
        idPhoto.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            idFile = file?.let { Compressor.compress(this@ConfirmationVehicleActivity, it) }
            idFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageId)
            }
        }
    }

    private fun requestPermissionsCompat(
        permissions: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage?.openChooser(this)
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage?.openCameraForImage(this)
        }
    }

    override fun showLoading() {
        layoutLoading.toVisible()
    }

    override fun hideLoading() {
        layoutLoading.toGone()
    }

    override fun showError(throwable: Throwable) {
        showOkDialog(throwable.message.toString(), "Oke", null)
    }

    override fun onResume() {
        super.onResume()
        val data = tinyDb?.getObject(TINY_LOCATION, LocationItem::class.java)
        if (data != null) {
            dataLocation = data
            editLocation.setText(data.branchAddress.toString())
        }
    }

    override fun onBackPressed() {
        if (unit1File != null && unit2File != null && idFile != null && dataLocation != null) {
            showYesNoDialog(
                "Yakin ingin kembali ?",
                "Iya",
                "Tidak",
                DialogInterface.OnClickListener { _, _ ->
                    super.onBackPressed()
                })
        } else {
            super.onBackPressed()
        }
    }
}