package id.jsu.suntiq.ui.intro.register.verify

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.readystatesoftware.chuck.internal.ui.MainActivity
import com.squareup.picasso.Picasso
import id.jsu.suntiq.R
import id.jsu.suntiq.api.response.register.DataRegister
import id.jsu.suntiq.api.response.register.RegisterResponse
import id.jsu.suntiq.api.response.register.verify.VerifyResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_REGISTER
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.HomeActivity
import id.jsu.suntiq.utils.LoaderIndicatorHelper
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import kotlinx.android.synthetic.main.register_verify_activity.*
import kotlinx.android.synthetic.main.toolbar_transparent.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File
import java.util.*

class VerifyRegisterActivity : BaseActivity(), VerifyContract.View {

    companion object {
        private val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
        private val CAMERA_REQUEST_CODE = 7500
    }

    private var easyImage: EasyImage? = null
    private var selviePhotos: ArrayList<MediaFile> = ArrayList()
    private var idPhotos: ArrayList<MediaFile> = ArrayList()

    private var loading: LoaderIndicatorHelper? = null
    private var selvieFile: File? = null
    private var idFile: File? = null
    private var code: Int? = 0

    private var presenter: VerifyPresenter? = null

    override fun provideLayout() {
        setContentView(R.layout.register_verify_activity)
    }

    override fun init(bundle: Bundle?) {
        presenter = VerifyPresenter(this)
        loading = LoaderIndicatorHelper.getInstance()
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initListener(bundle: Bundle?) {
        buttonBack.setOnClickListener { onBackPressed() }
        imageUploadSelfie.setOnClickListener {
            code = 1
            openCamera()
        }

        imageUploadId.setOnClickListener {
            code = 2
            openCamera()
        }

        buttonNextSelfie.setOnClickListener {
            if (selvieFile != null) {
                layoutSelvie.toGone()
                layoutId.toVisible()
            }
        }

        buttonNextId.setOnClickListener {
            selvieFile?.let { it1 -> idFile?.let { it2 -> presenter?.verify(it1, it2, TinyDB(this).getObject(TINY_REGISTER, DataRegister::class.java).id.toString()) } }
            showLoading()
        }

        buttonChangeSelfie.setOnClickListener {
            showYesNoDialog(
                "Ingin merubah Foto ?",
                "Iya",
                "Tidak",
                DialogInterface.OnClickListener { dialog, which ->
                    selvieFile = null
                    code = 1
                    openCamera()
                })
        }

        buttonChangeId.setOnClickListener {
            showYesNoDialog(
                "Ingin merubah Foto ?",
                "Iya",
                "Tidak",
                DialogInterface.OnClickListener { dialog, which ->
                    idFile = null
                    code = 2
                    openCamera()
                })
        }

        buttonNextSuccess.setOnClickListener {
            goToActivity(MainActivity::class.java)
            finishAffinity()
            TinyDB(this).remove(TINY_REGISTER)
        }
    }

    override fun getVerify(response: VerifyResponse) {
        if (response.status == 200) {
            layoutId.toGone()
            layoutSuccess.toVisible()
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
            requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE)
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
                    if (code == 1) {
                        onPhotosSelfie(imageFiles)
                    } else if (code == 2) {
                        onPhotosId(imageFiles)
                    }
                }
            })
    }

    private fun onPhotosSelfie(returnedPhotos: Array<MediaFile>) {
        selviePhotos.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            selvieFile = returnedPhotos[x].file
        }
        selvieFile?.let {
            Picasso.get()
                .load(it)
                .fit()
                .into(imageUploadSelfie)
        }
        buttonChangeSelfie.toVisible()
        textUploadedSelfie.toVisible()
    }

    private fun onPhotosId(returnedPhotos: Array<MediaFile>) {
        idPhotos.addAll(returnedPhotos)
        for (x in returnedPhotos.indices) {
            idFile = returnedPhotos[x].file
        }
        idFile?.let {
            Picasso.get()
                .load(it)
                .fit()
                .into(imageUploadId)
        }
        buttonChangeId.toVisible()
        textUploadedId.toVisible()
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
        loading?.showDialog(this)
    }

    override fun hideLoading() {
        loading?.dismissDialog()
    }

    override fun showError(throwable: Throwable) {
        showOkDialog(throwable.message.toString(), "Yes", null)
    }
}