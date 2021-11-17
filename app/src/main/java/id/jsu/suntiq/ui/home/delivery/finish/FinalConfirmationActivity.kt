package id.jsu.suntiq.ui.home.delivery.finish

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.vehicle.confirmation.FinishConfirmationRequest
import id.jsu.suntiq.api.response.vehicle.finish.FinishConfirmationResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM_DELIVERY
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.HomeActivity
import id.jsu.suntiq.ui.home.cancel.CancelActivity
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.final_confirmation_activity.*
import kotlinx.android.synthetic.main.final_confirmation_activity.buttonCancel
import kotlinx.android.synthetic.main.final_confirmation_activity.buttonSubmit
import kotlinx.android.synthetic.main.final_confirmation_activity.layoutLoading
import kotlinx.android.synthetic.main.header.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File
import java.util.ArrayList
import kotlinx.coroutines.launch

class FinalConfirmationActivity: BaseActivity(), FinishConfirmationContract.View {

    private var code: Int = 0

    companion object {
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
        private const val CAMERA_REQUEST_CODE = 7500
    }

    private var easyImage: EasyImage? = null
    private var takeOver: ArrayList<MediaFile> = ArrayList()
    private var unit1: ArrayList<MediaFile> = ArrayList()
    private var unit2: ArrayList<MediaFile> = ArrayList()
    private var unit3: ArrayList<MediaFile> = ArrayList()
    private var unit4: ArrayList<MediaFile> = ArrayList()
    private var bastk: ArrayList<MediaFile> = ArrayList()
    private var signedBastk: ArrayList<MediaFile> = ArrayList()
    private var withUser: ArrayList<MediaFile> = ArrayList()

    private var takeOverFile: File? = null
    private var unit1File: File? = null
    private var unit2File: File? = null
    private var unit3File: File? = null
    private var unit4File: File? = null
    private var bastkFile: File? = null
    private var signedBastkFile: File? = null
    private var withUserFile: File? = null

    private var presenter: FinishConfirmationPresenter? = null
    private var id: String? = ""

    override fun provideLayout() = setContentView(R.layout.final_confirmation_activity)

    override fun init(bundle: Bundle?) {
        presenter = FinishConfirmationPresenter(this)
    }

    override fun initData(bundle: Bundle?) {
        textName.text = getDataUser()?.username
        textPhone.text = getDataUser()?.phone_number
        id = getDataExistingProgress()?.id.toString()
    }

    override fun initListener(bundle: Bundle?) {
        imageTakeOver.setOnClickListener { openCamera(1) }
        imageUni1.setOnClickListener { openCamera(2) }
        imageUni2.setOnClickListener { openCamera(3) }
        imageUni3.setOnClickListener { openCamera(4) }
        imageUni4.setOnClickListener { openCamera(5) }
        imageSignature.setOnClickListener { openCamera(6) }
        imagePhotoSign.setOnClickListener { openCamera(7) }
        imagePhotoWithUser.setOnClickListener { openCamera(8) }
        buttonCancel.setOnClickListener {
            cancelData()
        }
        buttonBack.setOnClickListener { onBackPressed() }

        buttonSubmit.setOnClickListener {
            submitData()
        }
        buttonDownloadBastk.setOnClickListener { downloadBastk() }
    }

    private fun downloadBastk() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://jsuonline.xyz/api/v1/apps/delivery/doc/bastk?assignment_id=$id")
        )
        startActivity(browserIntent)
    }

    private fun submitData() {
        if (takeOverFile != null && unit1File != null && unit2File != null && unit3File != null && unit4File != null && bastkFile != null && signedBastkFile != null && withUserFile != null) {
            showLoading()
            presenter?.submitDataConfirmation(FinishConfirmationRequest(
                getDataExistingProgress()?.id.toString(),
                takeOverFile,
                unit1File,
                unit2File,
                unit3File,
                unit4File,
                bastkFile,
                signedBastkFile,
                withUserFile
            ))
        } else {
            showOkDialog("Photo harus di isi semua!", "Oke", null)
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
        intent.putExtra("id", getDataExistingProgressDelivery()?.id)
        startActivity(intent)
        finish()
    }

    override fun getDataConfirmation(response: FinishConfirmationResponse) {
        if (response.status == 200) {
            showOkDialog("Data berhasil terkirim dan akan segera diproses...", "Oke", DialogInterface.OnClickListener { _, _ ->
                gotoDashboard()
            })
        }
    }

    private fun gotoDashboard() {
        goToActivity(HomeActivity::class.java)
        TinyDB(this).remove(TINY_TEMPORARY_DATA_CONFIRM)
        TinyDB(this).remove(TINY_TEMPORARY_DATA_CONFIRM_DELIVERY)
        finishAffinity()
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

    private fun openCamera(value: Int) {
        code = value
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
                            onPhotosTakeOver(imageFiles)
                        }
                        2 -> {
                            onPhotosUnit1(imageFiles)
                        }
                        3 -> {
                            onPhotosUnit2(imageFiles)
                        }
                        4 -> {
                            onPhotosUnit3(imageFiles)
                        }
                        5 -> {
                            onPhotosUnit4(imageFiles)
                        }
                        6 -> {
                            onPhotosBastk(imageFiles)
                        }
                        7 -> {
                            onPhotosSignedBastk(imageFiles)
                        }
                        8 -> {
                            onPhotosWithUser(imageFiles)
                        }
                    }
                }
            })
    }

    private fun onPhotosTakeOver(returnedPhotos: Array<MediaFile>) {
        takeOver.addAll(returnedPhotos)
        var file : File? = null
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            takeOverFile = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            takeOverFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageTakeOver)
            }
        }
        buttonDownloadBastk.toVisible()
    }

    private fun onPhotosUnit1(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        unit1.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit1File = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            unit1File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUni1)
            }
        }
    }

    private fun onPhotosUnit2(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        unit2.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit2File = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            unit2File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUni2)
            }
        }
    }

    private fun onPhotosUnit3(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        unit3.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit3File = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            unit3File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUni3)
            }
        }
    }

    private fun onPhotosUnit4(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        unit4.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            unit4File = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            unit4File?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageUni4)
            }
        }
    }

    private fun onPhotosBastk(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        bastk.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            bastkFile = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            bastkFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imageSignature)
            }
        }
    }

    private fun onPhotosSignedBastk(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        signedBastk.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            signedBastkFile = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            signedBastkFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imagePhotoSign)
            }
        }
    }

    private fun onPhotosWithUser(returnedPhotos: Array<MediaFile>) {
        var file : File? = null
        withUser.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            file = returnedPhotos[x].file
        }
        lifecycleScope.launch {
            withUserFile = file?.let { Compressor.compress(this@FinalConfirmationActivity, it) }
            withUserFile?.let {
                Picasso.get()
                    .load(it)
                    .fit()
                    .into(imagePhotoWithUser)
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