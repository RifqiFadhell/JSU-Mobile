package id.jsu.suntiq.ui.home.delivery

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.vehicle.confirmation.ConfirmationOfficeRequest
import id.jsu.suntiq.api.response.vehicle.delivery.ConfirmationDeliveryResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM_DELIVERY
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.cancel.CancelActivity
import id.jsu.suntiq.ui.home.delivery.finish.FinalConfirmationActivity
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.delivery_activity.*
import kotlinx.android.synthetic.main.header.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File
import java.util.*
import kotlinx.coroutines.launch

class DeliveryActivity : BaseActivity(), DeliveryContract.View {

    private var code: Int = 0

    companion object {
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
        private const val CAMERA_REQUEST_CODE = 7500
    }

    private var easyImage: EasyImage? = null
    private var office: ArrayList<MediaFile> = ArrayList()
    private var receiver: ArrayList<MediaFile> = ArrayList()
    private var numberChasis: ArrayList<MediaFile> = ArrayList()

    private var officeFile: File? = null
    private var receiverFile: File? = null
    private var numberFile: File? = null

    private var presenter: DeliveryPresenter? = null

    override fun provideLayout() = setContentView(R.layout.delivery_activity)

    override fun init(bundle: Bundle?) {
        renderDataLocation()
        presenter = DeliveryPresenter(this)
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener(bundle: Bundle?) {
        imageOffice.setOnClickListener {
            code = 1
            openCamera()
        }
        imageReceiver.setOnClickListener {
            code = 2
            openCamera()
        }
        imageNumber.setOnClickListener {
            code = 3
            openCamera()
        }
        buttonCancel.setOnClickListener {
            cancelData()
        }
        buttonBack.setOnClickListener { onBackPressed() }

        buttonSubmit.setOnClickListener {
            submitData()
        }
    }

    private fun submitData() {
        showLoading()
        val name = editName.editableText.toString()
        val phone = editPhone.editableText.toString()
        val role = editRole.editableText.toString()
        when {
            name.isEmpty() -> {
                textInputName.error = "Nama penerima tidak boleh kosong"
            }
            phone.isEmpty() -> {
                textInputPhone.error = "Nomor tidak boleh kosong"
            }
            role.isEmpty() -> {
                textInputRole.error = "Role tidak boleh kosong"
            }
            else -> {
                textInputName.error = null
                textInputPhone.error = null
                textInputRole.error = null
            }
        }
        if (officeFile != null && receiverFile != null && numberFile != null) {
            presenter?.submitDataConfirmation(
                ConfirmationOfficeRequest(
                    getDataExistingProgress()?.id.toString(),
                    name,
                    phone,
                    role,
                    officeFile, receiverFile, numberFile
                )
            )
        } else {
            showOkDialog("Foto harus di isi semua", "Oke", null)
        }
    }

    override fun showLoading() {
        layoutLoading.toVisible()
    }

    override fun hideLoading() {
        layoutLoading.toGone()
    }

    override fun showError(throwable: Throwable) {
        showOkDialog(throwable.message.orEmpty(), "Oke", null)
    }

    override fun getDataConfirmation(response: ConfirmationDeliveryResponse) {
        if (response.status == 200) {
            TinyDB(this).putObject(TINY_TEMPORARY_DATA_CONFIRM_DELIVERY, response.data)
            goToActivity(FinalConfirmationActivity::class.java)
            finish()
        }
    }

    override fun onBackPressed() {
        showYesNoDialog(
            "Ingin membatalkan proses ?",
            "Iya",
            "Tidak",
            DialogInterface.OnClickListener { _, _ ->
                cancelData()
            })
    }

    private fun cancelData() {
        val intent = Intent(this, CancelActivity::class.java)
        intent.putExtra("id", getDataExistingProgress()?.id)
        startActivity(intent)
        finish()
    }

    private fun renderDataLocation() {
        val data = getDataExistingProgress()
        textName.text = getDataUser()?.username
        textPhone.text = getDataUser()?.phone_number
        textNameBuilding.text = data?.office?.officeName
        textOffice.text = data?.office?.branch
        textBranch.text = data?.office?.branchAddress
        textPic.text = data?.office?.branchPic
        textNumberPic.text = data?.office?.picContact
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
                necessaryPermissions, CAMERA_REQUEST_CODE
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
        office.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            officeFile = file?.let { Compressor.compress(this@DeliveryActivity, it) }
            officeFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageOffice)
            }
        }
    }

    private fun onPhotosUnit2(returnedPhotos: Array<MediaFile>) {
        receiver.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            receiverFile = file?.let { Compressor.compress(this@DeliveryActivity, it) }
            receiverFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageReceiver)
            }
        }
    }

    private fun onPhotosId(returnedPhotos: Array<MediaFile>) {
        numberChasis.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            numberFile = file?.let { Compressor.compress(this@DeliveryActivity, it) }
            numberFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageNumber)
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
}