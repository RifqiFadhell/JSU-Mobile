package id.jsu.suntiq.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import id.jsu.suntiq.R

class LoaderIndicatorHelper {

    private lateinit var progressDialog: AlertDialog
    val isShowing: Boolean
        get() = progressDialog.isShowing

    fun showDialog(context: Context) {
        progressDialog = AlertDialog.Builder(context).create()
        //if null or is showing then end the function and doing nothing
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        //check is activity still running?
        if (!(context as Activity).isFinishing) {
            progressDialog.show()
            progressDialog.setContentView(R.layout.general_simple_loading_dialog)

            val window = progressDialog.window
            if (window != null) {
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
            }
        }
    }

    fun dismissDialog() {
        try {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private var instance: LoaderIndicatorHelper? = null

        fun getInstance(): LoaderIndicatorHelper {
            if (instance == null) {
                instance = LoaderIndicatorHelper()
            }
            return instance as LoaderIndicatorHelper
        }
    }
}
