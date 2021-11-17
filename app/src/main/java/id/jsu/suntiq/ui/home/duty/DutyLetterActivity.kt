package id.jsu.suntiq.ui.home.duty

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.jsu.suntiq.R
import id.jsu.suntiq.ui.home.cancel.CancelActivity
import id.jsu.suntiq.ui.home.delivery.DeliveryActivity
import id.jsu.suntiq.utils.TimeUtils
import id.jsu.suntiq.utils.UnitFilter.convertToUri
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.*
import kotlinx.android.synthetic.main.duty_letter_activity.*
import java.io.File
import kotlinx.android.synthetic.main.toolbar_main.buttonBack

class DutyLetterActivity : BaseActivity() {

    private var imageUrl: String? = null
    private var webView: WebView? = null

    override fun provideLayout() {
        setContentView(R.layout.duty_letter_activity)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun init(bundle: Bundle?) {
        webView = findViewById(R.id.reader_view)
        if (getDataExistingProgress() != null) {
            imageUrl = getDataExistingProgress()?.docs?.suratJalan.orEmpty()
            initPdf()
        }
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initListener(bundle: Bundle?) {
        buttonShare.setOnClickListener {
            val bitmap = reader_view.getBitmapFromView()
            val uri = bitmap.convertToUri(this)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/jpeg"
            startActivity(intent)
        }

        buttonCancel.setOnClickListener {
            cancelData()
        }
        buttonBack.setOnClickListener { onBackPressed() }
        buttonSubmit.setOnClickListener {
            goToActivity(DeliveryActivity::class.java)
            finish()
        }
        buttonRefresh.setOnClickListener {
            initPdf()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initPdf() {
        val webSettings = webView?.settings
        webView?.settings?.setSupportZoom(true)
        webSettings?.javaScriptEnabled = true
        webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.orEmpty())
                hideRefresh()
                showLoading()
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
                hideRefresh()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
                showRefresh()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
            }
        }
        webView?.loadUrl("https://docs.google.com/gview?embedded=true&url=$imageUrl")
    }

    private fun showLoading() {
        loading.toVisible()
    }
    private fun hideLoading() {
        loading.toGone()
    }
    private fun showRefresh() {
        buttonRefresh.toVisible()
    }
    private fun hideRefresh() {
        buttonRefresh.toGone()
    }

    private fun download() {
        val bitmap = reader_view.getBitmapFromView()
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "surat_jalan-${TimeUtils.getTime()}.png").writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 85)
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
            showLongToast("Berhasil Menyimpan Dokumen di Folder Download")
        }
    }

    override fun onBackPressed() {
        showYesNoDialog("Ingin membatalkan proses ?", "Iya", "Tidak", DialogInterface.OnClickListener { _, _ ->
            cancelData()
        })
    }

    private fun cancelData() {
        val intent = Intent(this, CancelActivity::class.java)
        intent.putExtra("id", getDataExistingProgress()?.id)
        startActivity(intent)
        finish()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        } else {
            download()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    download()
                }
                return
            }
            else -> {}
        }
    }
    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}